package be.vives.ti.nfc_aanwezigheid

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.FormatException
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {

    private var adapter: NfcAdapter? = null
    var tag: WritableTag? = null
    var tagId: String? = null
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNfcAdapter()
        initViews()
    }

    private fun initNfcAdapter() {
        val nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager
        adapter = nfcManager.defaultAdapter
    }

    private fun initViews() {
        write_tag.setOnClickListener {
            writeNDefMessage()
        }
    }

    private fun writeNDefMessage() {
        val message = NfcUtils.prepareMessageToWrite(StringUtils.randomString(44), this)
        val writeResult = tag!!.writeData(tagId!!, message)
        if (writeResult) {
            showToast("Write successful!")
        } else {
            showToast("Write failed!")
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        disableNfcForegroundDispatch()
        super.onPause()
    }

    private fun enableNfcForegroundDispatch() {
        try {
            val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val nfcPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            adapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
        } catch (ex: IllegalStateException) {
            Log.e(getTag(), "Error enabling NFC foreground dispatch", ex)
        }
    }

    private fun disableNfcForegroundDispatch() {
        try {
            adapter?.disableForegroundDispatch(this)
        } catch (ex: IllegalStateException) {
            Log.e(getTag(), "Error disabling NFC foreground dispatch", ex)
        }
    }

    private fun getTag() = "MainActivity"

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tagFromIntent = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        try {
            tag = tagFromIntent?.let { WritableTag(it) }
        } catch (e: FormatException) {
            Log.e(getTag(), "Unsupported tag tapped", e)
            return
        }
        tagId = tag!!.tagId
        val aanwezigheid = Aanwezigheid(tagId, java.sql.Timestamp(System.currentTimeMillis()), "u0807613")
        val aanwezigheidString = Gson().toJson(aanwezigheid)
        Log.i("TESTETSTES", aanwezigheidString)
        Log.i("TESTETSTES", aanwezigheid.toString())
        database = Firebase.database.reference
        database.child("Aanwezigheid").setValue(tagId)
        showToast("Tag tapped: $tagId")

//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
//            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
//            if (rawMsgs != null) {
//                onTagTapped(NfcUtils.getUID(intent), NfcUtils.getData(rawMsgs))
//            }
//        }
    }

    private fun onTagTapped(superTagId: String, superTagData: String) {
        tag_uid.text = superTagId
        tag_data.text = superTagData
    }
}

package be.vives.ti.nfc_aanwezigheid

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private var intentFiltersArray: Array<IntentFilter>? = null
    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            if (nfcAdapter == null) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("This device doesn't support NFC.")
                builder.setPositiveButton("Cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()
                //txtviewshopid.setText("THIS DEVICE DOESN'T SUPPORT NFC. PLEASE TRY WITH ANOTHER DEVICE!")
                //txtviewmachineid.visibility = View.INVISIBLE

            } else if (!nfcAdapter!!.isEnabled) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("NFC Disabled")
                builder.setMessage("Plesae Enable NFC")
                //txtviewshopid.setText("NFC IS NOT ENABLED. PLEASE ENABLE NFC IN SETTINGS->NFC")
                //txtviewmachineid.visibility = View.INVISIBLE

                builder.setPositiveButton("Settings") { _, _ -> startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) }
                builder.setNegativeButton("Cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()
            }
        }
        catch (ex: Exception){
            Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray)
    }

}
package softfarm.helpers.importexport


import android.content.Context
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


/**
 * Created by duke0808 on 18.11.16.
 * Used to create and extract arvhives
 */

class ZipHelper {
    internal var zos: ZipOutputStream? = null
    internal var zis: ZipInputStream? = null
    var zipFile: File? = null
        private set

    @Throws(FileNotFoundException::class)
    constructor(zipFile: File) {
        this.zipFile = zipFile
        zis = ZipInputStream(FileInputStream(zipFile))
    }

    constructor()

    @Throws(IOException::class)
    fun createZipFile(context: Context, prefix: String = "Archive"): ZipHelper {
        val name = String.format(prefix, SimpleDateFormat("dd.MM.yy-HH.mm", Locale.getDefault()).format(Date()))
        zipFile = File(context.filesDir, name)
        zos = ZipOutputStream(FileOutputStream(zipFile!!))
        return this
    }

    @Throws(IOException::class)
    fun addFileToZip(file: File): ZipHelper {
        val fileInputStream = FileInputStream(file)
        val entry = ZipEntry(file.name)
        zos!!.putNextEntry(entry)
        fileInputStream.copyTo(zos!!)
        zos!!.closeEntry()
        fileInputStream.close()
        return this
    }

    fun getNextfileFromZip(context: Context): File? {
        if (zipFile == null || zis == null) {
            return null
        }

        var nextEntry: ZipEntry? = null
        try {
            nextEntry = zis!!.nextEntry
        } catch (e: EOFException) {
            return null
        }

        if (nextEntry == null) {
            return null
        }
        val name = nextEntry.name
        var ext = ""
        if (name.endsWith(".szip")) {
            ext = ".szip"
        }
        val file = File(context.filesDir, name)
        val fos = FileOutputStream(file)
        zis!!.copyTo(fos)
        zis!!.closeEntry()
        fos.close()
        return file
    }

    fun clear() {
        try {
            if (zis != null) {
                zis!!.close()
            }
            if (zos != null) {
                zos!!.close()
            }
            zipFile!!.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

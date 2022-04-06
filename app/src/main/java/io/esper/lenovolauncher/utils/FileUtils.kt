@file:Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION"
)

package io.esper.lenovolauncher.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.FileUtilsTag
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


object FileUtils {
    fun readJSONDataFromFile(context: Context): String {
        var inputStream: InputStream? = null
        val builder = java.lang.StringBuilder()
        try {
            var jsonString: String?
            val path = Constants.InternalRootFolder + "hospitaldb.json"
            Log.d("TAG", path)
            inputStream = FileInputStream(File(path))
            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonString = it } != null) {
                builder.append(jsonString)
            }
        } catch (e: Exception) {
            Log.d("TAG", e.message)
        } finally {
                inputStream?.close()
        }
        return String(builder)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("StaticFieldLeak")
    class Decompress(
        private var _ctx: Context,
        private var _zipFile: String,
        private var _location: String
    ) : AsyncTask<Void, Int, Boolean>() {
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            progressDialog = ProgressDialog(_ctx)
            progressDialog!!.setTitle("Unzipping")
            progressDialog!!.setMessage("Please wait...")
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        override fun onProgressUpdate(vararg values: Int?) {
        }

        override fun onPostExecute(result: Boolean?) {
            progressDialog!!.dismiss()
        }

        override fun doInBackground(vararg p0: Void?): Boolean {
            var zis: ZipInputStream? = null
            try {
                zis = ZipInputStream(BufferedInputStream(FileInputStream(_zipFile)))
                var ze: ZipEntry
                var count: Int
                val buffer = ByteArray(8192)
                while (zis.nextEntry.also { ze = it } != null) {
                    if (ze.name != null) {
                        var fileName: String = ze.name
                        fileName = fileName.substring(fileName.indexOf("/") + 1)
                        val file = File(_location, fileName)
                        val dir = if (ze.isDirectory) file else file.parentFile
                        if (!dir.isDirectory && !dir.mkdirs()) throw FileNotFoundException("Invalid path: " + dir.absolutePath)
                        if (ze.isDirectory) continue
                        FileOutputStream(file).use { fout ->
                            while (zis.read(buffer).also { count = it } != -1) fout.write(
                                buffer,
                                0,
                                count
                            )
                        }
                    } else
                        return true
                }
            } catch (ioe: Exception) {
                Log.d(FileUtilsTag, ioe.toString())
                return false
            } finally {
                if (zis != null) try {
                    zis.close()

                } catch (e: IOException) {
                }
            }
            return true
        }
    }
}
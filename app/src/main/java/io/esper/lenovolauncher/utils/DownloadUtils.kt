package io.esper.lenovolauncher.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.DownloadUtilsTag
import io.karn.notify.Notify
import kotlin.math.min

@Suppress("DEPRECATION")
object DownloadUtils {
    @SuppressLint("SetTextI18n")
    fun downloadContent(
        mContext: Context,
        url: String,
        downloadImg: ImageView,
        downloadTxt: TextView,
        fileName: String,
        id: Int,
        downloadBtn: LinearLayout
    ) {
        var prevProgress = 0
        initFileDownloader(mContext)
        PRDownloader.download(url, Constants.InternalRootFolder, fileName)
            .build()
            .setOnStartOrResumeListener {
                Log.d(DownloadUtilsTag, "Download Started $fileName")
                downloadBtn.isEnabled = false
                downloadImg.visibility = View.GONE
                downloadTxt.visibility = View.VISIBLE
                downloadStartNotification(mContext, fileName, id)
            }
            .setOnPauseListener { }
            .setOnCancelListener { }
            .setOnProgressListener {
                val progressPercent: Long = it.currentBytes * 100 / it.totalBytes
                if (progressPercent.toInt() > prevProgress) {
                    prevProgress = progressPercent.toInt()
                    downloadTxt.text = "$prevProgress%"
                }
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Log.d(DownloadUtilsTag, "Download Completed $fileName")
                    downloadCompleteNotification(mContext, fileName, id)
//                        downloadImg.setImageResource(R.drawable.complete)
                    downloadImg.visibility = View.VISIBLE
                    downloadTxt.visibility = View.GONE
//                    EventBus.getDefault().post(
//                        MainActivity.RefreshNeeded("wallpaper")
//                    )
                }

                override fun onError(error: com.downloader.Error?) {
                    Log.d(
                        DownloadUtilsTag,
                        "Download Error : Response Code :${error!!.responseCode} and Message :${error.serverErrorMessage}"
                    )
                    if (error.isServerError)
                        Log.d(DownloadUtilsTag, "Download Error : Server issue: ${error.isServerError}")
                    if (error.isConnectionError)
                        Log.d(DownloadUtilsTag, "Download Error : Connection issue: ${error.isConnectionError}")

//                        downloadImg.setImageResource(R.drawable.ic_cloud_download)
                }
            })
    }

    private fun initFileDownloader(mContext: Context) {
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(mContext.applicationContext, config)
    }

    private fun downloadStartNotification(mContext: Context, fileName: String, id: Int) {
        Notify.with(mContext).asBigText {
            title = "Downloading $fileName"
            expandedText = "We'll notify you once the download is complete"
            bigText = ""
        }
            .progress {
                showProgress = true
            }.show(id)
    }

    fun downloadCompleteNotification(mContext: Context, fileName: String, id: Int) {
        Notify.cancelNotification(mContext, id)
        val newFileName: String = if (fileName.length > 15)
            fileName.substring(0, min(fileName.length, 10)) + "..."
        else
            fileName

        Notify.with(mContext).content {
            title = "Download Complete"
            text = "$newFileName can be viewed in Files App"
        }.show(id)
    }
}
package dev.yanki.app.share

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.core.content.FileProvider
import dev.yanki.app.data.Mission
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

object ShareCardExporter {
    private const val W = 1080
    private const val H = 1920
    private val paper = Color.rgb(248, 248, 245)
    private val ink = Color.rgb(22, 26, 30)
    private val lime = Color.rgb(200, 252, 38)
    private val blue = Color.rgb(40, 60, 235)

    fun create(context: Context, mission: Mission, firstMs: Int, secondMs: Int): android.net.Uri {
        val bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawColor(paper)

        fun rect(l: Float, t: Float, r: Float, b: Float, radius: Float, color: Int) {
            paint.color = color
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(RectF(l, t, r, b), radius, radius, paint)
        }
        fun line(text: String, x: Float, y: Float, size: Float, color: Int, bold: Boolean = false) {
            paint.color = color
            paint.style = Paint.Style.FILL
            paint.typeface = Typeface.create("sans-serif", if(bold) Typeface.BOLD else Typeface.NORMAL)
            paint.textSize = size
            canvas.drawText(text, x, y, paint)
        }
        rect(70f, 74f, 360f, 156f, 40f, lime)
        line("YANKI", 104f, 136f, 58f, ink, true)
        line("KONUŞ • TEKRAR ET • GELİŞ", 72f, 248f, 33f, blue, true)
        line("Sesini", 72f, 410f, 133f, ink, true)
        line("duyur.", 72f, 550f, 133f, ink, true)

        rect(70f, 640f, 1010f, 1230f, 62f, Color.WHITE)
        line("BUGÜNÜN GÖREVİ", 126f, 742f, 39f, blue, true)
        val title = mission.title.take(34)
        line(title, 126f, 830f, if(title.length > 22) 53f else 65f, ink, true)
        line("İlk kayıt", 126f, 961f, 38f, ink)
        line(String.format(Locale.US,"%.1f sn",firstMs/1000.0), 705f, 961f, 50f, blue, true)
        line("Sonraki kayıt", 126f, 1074f, 38f, ink)
        line(if(secondMs > 0) String.format(Locale.US,"%.1f sn",secondMs/1000.0) else "Henüz yok",
            705f,1074f,if(secondMs > 0) 50f else 33f, blue, true)
        line("Süreler ölçüldü; akıcılık puanı değil.", 126f, 1161f, 26f, Color.DKGRAY)

        rect(70f, 1295f, 1010f, 1580f, 62f, lime)
        line("Kendi sesim.", 126f, 1400f, 65f, ink, true)
        line("Kendi gelişimim.", 126f, 1496f, 65f, ink, true)

        line("YANKI  /  KONUŞMA GÜNLÜĞÜ", 74f, 1753f, 34f, blue, true)
        line("Kısa görevler. Gerçek denemeler.", 74f, 1813f, 30f, ink)
        line("Bu kart kişisel kayıt sürelerinden üretildi.", 74f, 1854f, 26f, Color.DKGRAY)

        val directory = File(context.cacheDir,"share_cards").apply { mkdirs() }
        val file = File(directory,"yanki_share_" + System.currentTimeMillis() + ".png")
        FileOutputStream(file).use { stream -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) }
        bitmap.recycle()
        return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
    }

    fun share(context: Context, mission: Mission, firstMs: Int, secondMs: Int) {
        val image = create(context,mission,firstMs,secondMs)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, image)
            clipData = ClipData.newUri(context.contentResolver,"YANKI",image)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent,"YANKI sonuç kartını paylaş"))
    }
}

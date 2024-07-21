import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

suspend fun uriToByteArray(context: Context, uri: Uri): ByteArray? = withContext(Dispatchers.IO) {

    val contentResolver: ContentResolver = context.contentResolver
    val byteArrayOutputStream = ByteArrayOutputStream()
    var byteArray: ByteArray? = null
    var inputStream: InputStream? = null

    try {
        inputStream = contentResolver.openInputStream(uri)
        val buffer = ByteArray(4 * 1024) // Adjust buffer size as needed
        var bytesRead: Int
        while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }
        byteArray = byteArrayOutputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
        byteArrayOutputStream.close()
    }

    byteArray
}


fun byteArrayToImage(byteArray: ByteArray): ImageBitmap {

    val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    val imageBitmap: ImageBitmap = bitmap.asImageBitmap()

    return imageBitmap
}

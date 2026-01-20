package cash.turbine.noir.stdlib

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileSystem
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class NoirStdlibVirtualFile(
    private val fileSystem: NoirStdlibFileSystem,
    private val file: File,
    private val relativePath: String
) : VirtualFile() {

    override fun getName(): String = file.name

    override fun getFileSystem(): VirtualFileSystem = fileSystem

    override fun getPath(): String = relativePath

    override fun isWritable(): Boolean = false

    override fun isDirectory(): Boolean = file.isDirectory

    override fun isValid(): Boolean = file.exists()

    override fun getParent(): VirtualFile? {
        val parentFile = file.parentFile ?: return null
        val parentPath = relativePath.substringBeforeLast('/', "")
        if (parentPath.isEmpty()) return null
        return NoirStdlibVirtualFile(fileSystem, parentFile, parentPath)
    }

    override fun getChildren(): Array<VirtualFile> {
        if (!isDirectory) return emptyArray()
        return file.listFiles()?.map { child ->
            val childPath = if (relativePath.isEmpty()) child.name else "$relativePath/${child.name}"
            NoirStdlibVirtualFile(fileSystem, child, childPath)
        }?.toTypedArray() ?: emptyArray()
    }

    override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long): OutputStream {
        throw UnsupportedOperationException("Stdlib files are read-only")
    }

    override fun contentsToByteArray(): ByteArray = file.readBytes()

    override fun getTimeStamp(): Long = file.lastModified()

    override fun getLength(): Long = file.length()

    override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) {
        postRunnable?.run()
    }

    override fun getInputStream(): InputStream = file.inputStream()

    override fun getModificationStamp(): Long = file.lastModified()
}

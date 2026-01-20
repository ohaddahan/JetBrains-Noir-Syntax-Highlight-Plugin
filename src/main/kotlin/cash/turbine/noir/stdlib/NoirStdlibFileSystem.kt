package cash.turbine.noir.stdlib

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileSystem
import java.io.File

class NoirStdlibFileSystem : VirtualFileSystem() {

    override fun getProtocol(): String = PROTOCOL

    override fun findFileByPath(path: String): VirtualFile? {
        val stdlibRoot = findStdlibRoot() ?: return null
        val file = File(stdlibRoot, path)
        if (!file.exists()) return null
        return NoirStdlibVirtualFile(this, file, path)
    }

    override fun refresh(asynchronous: Boolean) {}

    override fun refreshAndFindFileByPath(path: String): VirtualFile? = findFileByPath(path)

    override fun addVirtualFileListener(listener: VirtualFileListener) {}

    override fun removeVirtualFileListener(listener: VirtualFileListener) {}

    override fun deleteFile(requestor: Any?, vFile: VirtualFile) {
        throw UnsupportedOperationException("Stdlib files are read-only")
    }

    override fun moveFile(requestor: Any?, vFile: VirtualFile, newParent: VirtualFile) {
        throw UnsupportedOperationException("Stdlib files are read-only")
    }

    override fun renameFile(requestor: Any?, vFile: VirtualFile, newName: String) {
        throw UnsupportedOperationException("Stdlib files are read-only")
    }

    override fun createChildFile(requestor: Any?, vDir: VirtualFile, fileName: String): VirtualFile {
        throw UnsupportedOperationException("Stdlib files are read-only")
    }

    override fun createChildDirectory(requestor: Any?, vDir: VirtualFile, dirName: String): VirtualFile {
        throw UnsupportedOperationException("Stdlib files are read-only")
    }

    override fun copyFile(requestor: Any?, virtualFile: VirtualFile, newParent: VirtualFile, copyName: String): VirtualFile {
        throw UnsupportedOperationException("Stdlib files are read-only")
    }

    override fun isReadOnly(): Boolean = true

    companion object {
        const val PROTOCOL = "noir-std"

        fun findStdlibRoot(): String? {
            val nargoPath = findNargoExecutable() ?: return null
            val nargoDir = File(nargoPath).parentFile?.parentFile ?: return null

            val possiblePaths = listOf(
                File(nargoDir, "lib/noir_stdlib/src"),
                File(nargoDir, "share/noir/stdlib"),
                File(nargoDir, "noir_stdlib/src"),
                File(System.getProperty("user.home"), ".nargo/lib/noir_stdlib/src"),
                File(System.getProperty("user.home"), ".noir/stdlib")
            )

            return possiblePaths.firstOrNull { it.exists() && it.isDirectory }?.absolutePath
        }

        private fun findNargoExecutable(): String? {
            val pathEnv = System.getenv("PATH") ?: return null
            val pathDirs = pathEnv.split(File.pathSeparator)
            val executableName = if (System.getProperty("os.name").lowercase().contains("win")) "nargo.exe" else "nargo"

            for (dir in pathDirs) {
                val file = File(dir, executableName)
                if (file.exists() && file.canExecute()) {
                    return file.absolutePath
                }
            }
            return null
        }

        fun getInstance(): NoirStdlibFileSystem? {
            return com.intellij.openapi.vfs.VirtualFileManager.getInstance()
                .getFileSystem(PROTOCOL) as? NoirStdlibFileSystem
        }
    }
}

package com.gibson.spica.data.repository

import com.gibson.spica.data.model.KmpFile
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    // 💡 The UI calls this to trigger the platform-specific picker
    suspend fun selectFileForUpload(mimeTypes: List<String>): KmpFile?

    // 💡 Uploads the file, providing a progress flow
    fun uploadFile(
        kmpFile: KmpFile, 
        storagePath: String
    ): Flow<Result<String>> // Returns flow of Result<DownloadUrl>
}

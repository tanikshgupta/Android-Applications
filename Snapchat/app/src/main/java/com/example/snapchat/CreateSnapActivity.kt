package com.example.snapchat

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.provider.MediaStore
import android.content.Intent
import android.view.View
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.NonNull
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*


class CreateSnapActivity : AppCompatActivity() {
    var createsnapImageView: ImageView? = null
    var messageEditText: EditText? = null
    val imageName = UUID.randomUUID().toString() + ".jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)

        setTitle("Create a snap")

        createsnapImageView = findViewById(R.id.createSnapImageView)
        messageEditText = findViewById(R.id.messageEditText)


    }

    fun nextClicked (view: View) {
        // Get the data from an ImageView as bytes
        createsnapImageView?.isDrawingCacheEnabled = true
        createsnapImageView?.buildDrawingCache()
        val bitmap = (createsnapImageView?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var downloadUrl: String? = null//FirebaseStorage.getInstance().getReference().child("images").child(imageName).downloadUrl.toString()


        var uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this, "Upload Unsuccessful :(", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...

            downloadUrl = uploadTask.snapshot.storage.downloadUrl.toString()
            Log.i("taniksh download taniksh url : ",downloadUrl)
            /*FirebaseStorage.getInstance().getReference().child("images").child(imageName).downloadUrl.addOnSuccessListener {
                downloadUrl = it.toString()
                Log.i("Geetting url","passed")

            }.addOnFailureListener{
                Log.i("Geetting url","failed")
            }*/
            /**/
            Toast.makeText(this, "Upload Successful :)", Toast.LENGTH_SHORT).show()


            val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                if(!task.isSuccessful) {
                    task.exception?.let {
                        throw  it
                    }
                }
                return@Continuation FirebaseStorage.getInstance().getReference().child("images").child(imageName).downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    downloadUrl = downloadUri.toString()
                    Log.i("Geetting url","passed : " + downloadUrl)
                    val intent = Intent(this, ChooseUserActivity::class.java)
                    Log.i("imageUrl",downloadUrl)
                    intent.putExtra("imageUrl",downloadUrl)
                    intent.putExtra("imageName",imageName)
                    intent.putExtra("message",messageEditText?.text.toString())
                    startActivity(intent)

                } else {
                    Log.i("Geetting url","failed")
                    Toast.makeText(this,"Something went wrong :(", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SnapsActivity::class.java)
                    startActivity(intent)
                }
            }




        }

    }

    fun getPhoto() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    fun chooseImageClicked(view: View) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1);
        } else{
            getPhoto();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data!!.data
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {

                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)

                createsnapImageView?.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }

}


package com.example.myapplicationcomp

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplicationcomp.ui.theme.MyApplicationCompTheme
import android.graphics.ImageDecoder
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val customColors = lightColorScheme(
                primary = Color(253, 85, 100), // Change primary color to red
                secondary = Color.Green, // Change secondary color to green
                background = Color.White, // Change background color to white
                onBackground = Color.Black, // Change text color on background to black
                surface = Color.White, // Change surface color to white
                onSurface = Color.Black, // Change text color on surface to black
                error = Color.Red, // Change error color to red
                onError = Color.White // Change text color on error to white
            )
            MaterialTheme(colorScheme = customColors) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen()

                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationCompTheme {
        ProfileScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    var name by remember { mutableStateOf("") }
    val primaryColor = MaterialTheme.colorScheme.primary // Get the primary color from the theme


    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    imageUri?.let {

        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Circular Image
        Box(
            modifier = Modifier
                .size(180.dp)
                // .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (imageUri == null) {

                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                       // .padding(4.dp)
                        .clip(CircleShape)
                        .background(Color(250, 246, 243))
                        //.background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))


                        .clickable {
                            launcher.launch("image/*")
                        },
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(primaryColor)


                )

                Image(painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .clickable {
                            launcher.launch("image/*")

                        },
                    colorFilter = ColorFilter.tint(primaryColor)

                )

        } else {


        bitmap.value?.let { btm ->

            Image(
                bitmap = btm.asImageBitmap(),
                contentDescription = null,

                modifier = Modifier
                    .size(180.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    .clickable {
                        launcher.launch("image/*")
                    },
                        alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth


                )


        }
    }
    }


        Spacer(modifier = Modifier.height(16.dp))

        // EditText for entering name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name", color = primaryColor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp,),
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = primaryColor, cursorColor = primaryColor, unfocusedBorderColor = primaryColor, )
        )


        // Save button
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            onClick = {
                Toast.makeText(context, "bitmap $bitmap imageuri $imageUri", Toast.LENGTH_LONG)
                    .show()
            },

            modifier = Modifier
                .align(Alignment.End)// Aligns the button to the bottom right corner
                .padding(end = 66.dp, top = 18.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp) // Adjust the size as needed
            )
        }


    }
}







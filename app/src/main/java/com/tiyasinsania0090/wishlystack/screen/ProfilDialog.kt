package com.tiyasinsania0090.wishlystack.screen

import android.content.Context
import androidx.credentials.CredentialManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.credentials.exceptions.ClearCredentialException
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.model.User
import com.tiyasinsania0090.wishlystack.ui.theme.WishlyStackTheme
import com.tiyasinsania0090.wishlystack.util.UserDataStore
import androidx.credentials.ClearCredentialStateRequest
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun ProfilDialog(
    user: User,
    onDismissRequest: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    val datastore = UserDataStore(context)
    val coroutineScope = rememberCoroutineScope()
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.pictureUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.profil),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.baseline_account_circle_24),
                    error = painterResource(id = R.drawable.baseline_account_circle_24),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = user.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user.email,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(stringResource(R.string.tombol_batal))
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {

                                signOut(context, datastore)

                                navController.navigate(Screen.Category.route)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(stringResource(R.string.logout))
                    }
                }
            }
        }
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore){
    try{
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException){
        Log.e("SIGN-IN", "Error: ${e.message}")
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilDialogPreview() {
    WishlyStackTheme {
        ProfilDialog(
            user = User(name = "Tiya Insania", email = "tiya.insania@example.com", pictureUrl = ""),
            onDismissRequest = {},
            rememberNavController()
        )
    }
}
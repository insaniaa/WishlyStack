package com.tiyasinsania0090.wishlystack.screen

import android.content.Context
import androidx.credentials.GetCredentialRequest
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tiyasinsania0090.wishlystack.model.User
import com.tiyasinsania0090.wishlystack.R
import com.tiyasinsania0090.wishlystack.component.BottomBar
import com.tiyasinsania0090.wishlystack.component.WishItem
import com.tiyasinsania0090.wishlystack.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.tiyasinsania0090.wishlystack.util.UserDataStore
import androidx.credentials.CredentialManager
import com.tiyasinsania0090.wishlystack.util.SettingDataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore1 = SettingDataStore(context)

    val dataStore = UserDataStore(context)

    val showList by dataStore1.layoutFlow.collectAsState(true)
    val user by dataStore.getUserFlow().collectAsState(initial = User("", "", ""))

    val factory = ViewModelFactory(context)
    val viewModel: WishViewModel = viewModel(factory = factory)
    val apiStatus by viewModel.apiWishlistState.collectAsState()

    var showProfilDialog by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, user.email) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (user.email.isNotEmpty()) {
                    viewModel.retrieveDataFromApi(user.email)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(user.email) {
        if (user.email.isNotEmpty()) {
            viewModel.retrieveDataFromApi(user.email)
        }
    }


    if (showProfilDialog) {
        ProfilDialog(
            user = user,
            onDismissRequest = { showProfilDialog = false },
            navController = navController
        )
    }

    Scaffold(
        containerColor = Color(0xFFF7F1FF),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.wishlist),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_info_outline_24),
                            contentDescription = stringResource(R.string.info)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore1.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (showList) R.drawable.baseline_view_list_24
                                else R.drawable.baseline_grid_view_24
                            ),
                            contentDescription = if (showList) stringResource(R.string.grid) else stringResource(R.string.list)
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Form.route) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.tambah),
                    tint = Color.White
                )
            }
        },

        bottomBar = {
            BottomBar(
                currentScreen = "wishlist",
                onListClick = { /* Tetap di halaman wishlist */ },
                onCategoryClick = { navController.navigate(Screen.Category.route) },
                onProfileClick = {
                    if (user.email.isNotEmpty()) {
                        showProfilDialog = true
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            signIn(context, dataStore)
                        }
                    }
                }

            )

        },
    ) { padding ->
        when (val status = apiStatus) {
            is ApiStatus.Loading -> {
                LoadingScreen(modifier = Modifier.padding(padding))
            }
            is ApiStatus.Error -> {
                ErrorScreen(
                    message = status.message,
                    onRetry = {
                        if (user.email.isNotEmpty()) {
                            viewModel.retrieveDataFromApi(user.email)
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
            is ApiStatus.Success -> {
                val data = status.wishlist
                if (data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Text("Anda belum memiliki wishlist.")
                    }
                } else {
                    AnimatedContent(
                        targetState = showList,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                        },
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) { isList ->
                        if (isList) {
                            LazyColumn {
                                items(items = data) { wish ->
                                    WishItem(
                                        wish = wish,
                                        isGrid = false,
                                        onDetailClick = {
                                            navController.navigate("detail/${wish.id}")
                                        }
                                    )
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(items = data) { wish ->
                                    WishItem(
                                        wish = wish,
                                        isGrid = true,
                                        onDetailClick = {
                                            navController.navigate("detail/${wish.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

suspend fun signIn(context: Context, dataStore: UserDataStore) {
    Log.d("Login", "Masuk ke sini")
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("869465864358-014br9nnotb2q8gog6iingviov3a8m3k.apps.googleusercontent.com")
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException){
        Log.e("SIGN-IN", "Error: ${e.message}")
    }
}

private suspend fun handleSignIn(result: androidx.credentials.GetCredentialResponse, dataStore: UserDataStore) {
    val credential = result.credential
    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException){
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }else{
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error: $message")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Coba Lagi")
        }
    }
}
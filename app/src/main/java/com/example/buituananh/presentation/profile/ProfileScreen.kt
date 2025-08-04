package com.example.buituananh.presentation.profile

import android.net.Uri
import android.provider.ContactsContract.Profile
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.domain.model.UserInformation
import com.example.buituananh.presentation.profile.item.InformationSection
import com.example.buituananh.presentation.profile.item.InputField
import com.example.buituananh.presentation.profile.item.SuccessfulDialog
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.ImageUtils
import kotlinx.coroutines.delay

@Composable
fun ProfileScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    var isShowDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                ProfileEffect.ShowDialog -> {
                    isShowDialog = true
                }

                is ProfileEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var enableEditor by remember { mutableStateOf(false) }
    LaunchedEffect(isShowDialog) {
        if (isShowDialog) {
            delay(2000L)
            isShowDialog = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileIntent.LoadUserData)
    }

    ProfileScreen(
        state = state,
        enableEditor = enableEditor,
        isShowDialog = isShowDialog,
        onIntent = viewModel::onIntent,
        onDismissDialog = { isShowDialog = false },
        onEnableEditorChange = { enableEditor = !enableEditor }
    )

}

@Composable
private fun ProfileScreen(
    state: ProfileState,
    enableEditor: Boolean,
    isShowDialog: Boolean,
    onIntent: (ProfileIntent) -> Unit,
    onDismissDialog: () -> Unit,
    onEnableEditorChange: () -> Unit
) {
    val currentFocusManager = LocalFocusManager.current
    val currentWidthScreen = LocalConfiguration.current.screenWidthDp.dp
    val scrollState = rememberScrollState()
    val focusRequester = remember {
        FocusRequester()
    }

    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                val uriPicker = ImageUtils.resizeImage(
                    context,
                    uri ?: Uri.EMPTY
                ) ?: Uri.EMPTY
                onIntent(ProfileIntent.PickImage(uriPicker))
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(34.dp))
        //information section
        InformationSection(
            enableEditor = enableEditor,
            uri = state.avatarUri,
            onAvatarChange = {
                launcher.launch("image/*")
            }
        ) {
            onEnableEditorChange()
            focusRequester.requestFocus()
        }
        Spacer(Modifier.height(28.dp))
        //name, phonenumber section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .padding(horizontal = 12.dp)
        ) {

            InputField(
                modifier = Modifier.width((currentWidthScreen - 32.dp) / 2),
                titleName = "Name",
                placeholderText = "Enter your name...",
                inputValue = state.name,
                isEnabled = enableEditor,
                isError = state.isNameError
            ) {
                onIntent(ProfileIntent.OnNameChange(it))
            }
            Spacer(Modifier.width(8.dp))
            InputField(
                modifier = Modifier.width((currentWidthScreen - 32.dp) / 2),
                titleName = "Phone number",
                placeholderText = "Your phone number...",
                inputValue = state.phoneNumber,
                isEnabled = enableEditor,
                isPhoneOptions = true,
                isError = state.isPhoneNumberError
            ) {
                onIntent(ProfileIntent.OnPhoneNumberChange(it))
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                titleName = "UNIVERSITY NAME",
                placeholderText = "Your university name...",
                inputValue = state.universityName,
                isEnabled = enableEditor,
                isError = state.isUniversityError
            ) {
                onIntent(ProfileIntent.OnUniversityNameChange(it))
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                titleName = "DESCRIBE YOURSELF",
                placeholderText = "Enter a description about yourself...",
                inputValue = state.description,
                maxLines = 4,
                isEnabled = enableEditor,
                isLastOne = true
            ) {
                onIntent(ProfileIntent.OnDescriptionChange(it))
            }
        }
        Spacer(Modifier.height(20.dp))
        //submit button
        if (enableEditor) {
            Button(
                onClick = {
                    onIntent(ProfileIntent.OnSubmitClick)
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Submit", modifier = Modifier.padding(
                        vertical = 8.dp,
                        horizontal = 24.dp
                    )
                )
            }
        }
        if (isShowDialog) {
            SuccessfulDialog {
                onDismissDialog()
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFF4FAFC)
@Composable
fun PreviewComposeHoisting(modifier: Modifier = Modifier) {
    BuiTuanAnhTheme {
//        Screen1()
    }
}


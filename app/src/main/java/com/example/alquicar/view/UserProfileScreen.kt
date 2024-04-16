import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.alquicar.viewmodel.UserProfileViewModel
import coil.compose.rememberImagePainter
import com.example.alquicar.model.UserProfile

@Composable
fun UserProfileScreen(viewModel: UserProfileViewModel = hiltViewModel()) {
    val authUser = FirebaseAuth.getInstance().currentUser
    val userProfile by viewModel.userProfile.collectAsState()

    // Efecto lanzado para cargar el perfil del usuario
    LaunchedEffect(authUser?.email) {
        authUser?.email?.let { email ->
            viewModel.loadUserProfileByEmail(email)
            Log.w("", "VIEW MODEL EMAIL:"+email)
        }
    }

    // Verificación de la autenticación del usuario
    if (authUser == null) {
        Text("No user is signed in.", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
    } else {
        userProfile?.let { user ->
            UserProfileDetails(user, viewModel)
        } ?: Text("Cargando perfil del usuario...", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun UserProfileDetails(user: UserProfile, viewModel: UserProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar la imagen del perfil del usuario, si está disponible
        user.imageUrl.takeIf { it.isNotEmpty() }?.let { url ->
            Image(
                painter = rememberImagePainter(url),
                contentDescription = "Imagen del perfil del usuario",
                modifier = Modifier
                    .size(150.dp)
                    .clip(MaterialTheme.shapes.medium)  // Circular o el recorte deseado
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar detalles del usuario
        Text("Nombre: ${user.firstName} ${user.lastName}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Email: ${user.email}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Teléfono: ${user.phoneNumber}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Ciudad: ${user.city}", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val updatedProfile = user?.copy(
                firstName = user.firstName, lastName = user.lastName, email = user.email, phoneNumber = user.phoneNumber
            )
            updatedProfile?.let { viewModel.updateUserProfile(it.userId, it) }
        }) {
            Text("Editar Perfil")
        }
    }
}
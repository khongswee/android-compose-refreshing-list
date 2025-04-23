package kh.sample.compose.refreshing_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kh.sample.compose.refreshing_list.MainActivity.Companion.KEY_REFRESH_TRIGGER
import kh.sample.compose.refreshing_list.ui.detail.DetailScreen
import kh.sample.compose.refreshing_list.ui.list.ListScreen
import kh.sample.compose.refreshing_list.ui.theme.SKRefreshingListTheme
import kotlinx.coroutines.flow.StateFlow


class MainActivity : ComponentActivity() {
    companion object {
        private const val LIST_SCREEN = "list_screen"
        private const val DETAIL_SCREEN = "detail_screen}"
        private const val MAIN_ROUTE = "main_route"
        const val KEY_REFRESH_TRIGGER = "refresh_trigger"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SKRefreshingListTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = MAIN_ROUTE) {
                    navigation(route = MAIN_ROUTE, startDestination = LIST_SCREEN) {

                        composable(LIST_SCREEN) {
                            val refreshTrigger =
                                getResultStateRefreshTrigger(navController)?.collectAsStateWithLifecycle()
                            ListScreen(
                                isRefreshing = refreshTrigger?.value ?: false,
                                onItemClick = { number ->
                                    clearRefresh(navController)
                                    navController.navigate("$DETAIL_SCREEN/${number}")
                                })
                        }

                        composable(
                            "${DETAIL_SCREEN}/{number}",
                            arguments = listOf(navArgument("number") {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val number = backStackEntry.arguments?.getInt("number") ?: 0
                            DetailScreen(number = number, onBack = {
                                navController.popBackStack()
                            }, onRefresh = {
                                updateRefreshTrigger(
                                    navController = navController
                                )
                                navController.popBackStack()
                            })
                        }

                    }
                }
            }
        }
    }
}

private fun updateRefreshTrigger(navController: NavHostController) {
    navController.previousBackStackEntry?.savedStateHandle?.set(KEY_REFRESH_TRIGGER, true)
}

private fun clearRefresh(navController: NavHostController) {
    navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>(KEY_REFRESH_TRIGGER)
}

private fun getResultStateRefreshTrigger(navController: NavHostController): StateFlow<Boolean>? {
    return navController.currentBackStackEntry?.savedStateHandle?.getStateFlow(
        KEY_REFRESH_TRIGGER,
        false
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SKRefreshingListTheme {
        Greeting("Android")
    }
}
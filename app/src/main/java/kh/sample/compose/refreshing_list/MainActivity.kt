package kh.sample.compose.refreshing_list

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import kh.sample.compose.refreshing_list.ui.list.ListScreen
import kh.sample.compose.refreshing_list.ui.theme.SKRefreshingListTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kh.sample.compose.refreshing_list.ui.detail.DetailScreen


class MainActivity : ComponentActivity() {
    companion object {
        private const val LIST_SCREEN = "list_screen"
        private const val DETAIL_SCREEN = "detail_screen/{number}"
        private const val MAIN_ROUTE = "main_route"
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
                            ListScreen(onItemClick = { number ->
                                navController.navigate("detail_screen/${number}")
                            })
                        }

                        composable(
                            DETAIL_SCREEN,
                            arguments = listOf(navArgument("number") {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val number = backStackEntry.arguments?.getInt("number") ?: 0
                            DetailScreen(number = number, onBack = {
                                navController.popBackStack()
                            }, onRefresh = {
                                navController.navigate(LIST_SCREEN) {
                                    popUpTo(LIST_SCREEN) {
                                        inclusive = true
                                    }
                                }
                            })
                        }

                    }
                }
            }
        }
    }
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
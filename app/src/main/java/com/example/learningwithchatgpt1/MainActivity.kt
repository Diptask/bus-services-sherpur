package com.dipta.sherpur_bus_service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dipta.sherpur_bus_service.R
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.*
import androidx.navigation.compose.*
import java.io.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.rememberInfiniteTransition


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningWithChatGpT1Theme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    // Load your logo from resources
    val logoImage = painterResource(id = R.drawable.logo) // Use the generated vector resource

    // Main UI
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // Logo placed at the top
            Image(
                painter = logoImage,
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            // Spacer between logo and content
            Spacer(modifier = Modifier.height(2.dp))
            // Navigation host for the app screens
            NavHost(navController = navController, startDestination = "greeting") {
                composable("greeting") { GreetingScreen(navController) }
                composable("newScreen") { NewScreen(navController) }
                composable(
                    "resultScreen/{startOption}/{endOption}/{timeOption}",
                    arguments = listOf(
                        navArgument("startOption") { type = NavType.StringType },
                        navArgument("endOption") { type = NavType.StringType },
                        navArgument("timeOption") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val startOption = backStackEntry.arguments?.getString("startOption") ?: ""
                    val endOption = backStackEntry.arguments?.getString("endOption") ?: ""
                    val timeOption = backStackEntry.arguments?.getString("timeOption") ?: ""
                    ResultScreen(startOption, endOption, timeOption, LocalContext.current, navController)
                }
            }
        }
    }
}



@Composable
fun GreetingScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("how can we help you today?") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Hello, $name!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("newScreen") }) {
                Text("বাসের সময়সূচী")
            }
        }
    }
}

@Composable
fun NewScreen(navController: NavHostController) {
    var startOption by remember { mutableStateOf("Sherpur") }
    var endOption by remember { mutableStateOf("Dhaka") }
    var timeOption by remember { mutableStateOf("রাত") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DropdownFilter(
                title = "যাত্রা শুরু",
                options = listOf("Sherpur", "Dhaka", "Savar", "Ashulia", "Narayanganj", "Chattogram", "Khulna", "North Bengal"),
                selectedOption = startOption,
                onOptionSelected = { startOption = it }
            )
            DropdownFilter(
                title = "গন্তব্য",
                options = listOf("Sherpur", "Dhaka", "Savar", "Ashulia", "Narayanganj", "Chattogram", "Khulna", "North Bengal"),
                selectedOption = endOption,
                onOptionSelected = { endOption = it }
            )
            DropdownFilter(
                title = "বাস ছাড়ার সময়",
                options = listOf("রাত", "ভোর", "সকাল", "দুপুর", "বিকাল"),
                selectedOption = timeOption,
                onOptionSelected = { timeOption = it }
            )
            Button(
                onClick = {
                    navController.navigate("resultScreen/$startOption/$endOption/$timeOption")
                },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text("Submit")
            }
        }
    }
}


@Composable
fun DropdownFilter(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = selectedOption, style = MaterialTheme.typography.bodyMedium)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ResultScreen(
    startOption: String,
    endOption: String,
    timeOption: String,
    context: Context,
    navController: NavHostController
) {
    val csvData = remember { readCsvFromAssets(context, "bus_sherpur.csv") }
    val header = csvData.firstOrNull()
    val filteredData = csvData.drop(1).filter { row ->
        row[6] == startOption && row[7] == endOption && row[5] == timeOption
    }

    // Remember scroll state
    val scrollState = rememberScrollState()

    // Calculate total content height
    val totalContentHeight = remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        totalContentHeight.value = filteredData.size * 48 // Assuming each row is 48dp in height
    }

    Column(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Display the header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                header?.take(5)?.forEach { cell ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(5.dp)
                            .height(36.dp) // Ensure consistent height
                    ) {
                        Text(
                            cell,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Display the filtered data
            filteredData.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        row.take(5).forEachIndexed { index, cell ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(BorderStroke(1.dp, Color.Black))
                                    .padding(3.dp)
                                    .height(60.dp) // Ensure consistent height
                            ) {
                                if (index == 3 || index == 4) {
                                    // Display phone number and call button
                                    val phoneNumber = row.getOrNull(index)
                                    phoneNumber?.let {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = phoneNumber,
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            ShimmeringIconButton(
                                                onClick = { makePhoneCall(context, phoneNumber) },
                                                modifier = Modifier.size(24.dp) // Adjust size as needed
                                                    .padding(start = 4.dp) // Add padding for alignment
                                                    .height(IntrinsicSize.Min)
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        cell,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                )
            }

            // TextButton to navigate back to the previous screen
            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.End)
            ) {
                Text("আবার সার্চ করুন")
            }
        }
    }
}





fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    context.startActivity(intent)
}

fun readCsvFromAssets(context: Context, fileName: String): List<List<String>> {
    val inputStream = context.assets.open(fileName)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val data = mutableListOf<List<String>>()

    reader.useLines { lines ->
        lines.forEach { line ->
            data.add(line.split(","))
        }
    }

    return data
}

@Composable
fun ShimmeringIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )

    IconButton(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Call,
            contentDescription = "Phone",
            tint = Color.Green // Customize the tint if needed
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LearningWithChatGpT1Theme {
        MyApp()
    }
}
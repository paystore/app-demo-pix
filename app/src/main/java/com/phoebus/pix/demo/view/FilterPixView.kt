package com.phoebus.pix.demo.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.data.enum.ReportType
import com.phoebus.pix.demo.ui.theme.components.Destinations
import com.phoebus.pix.demo.utils.DateUtils
import com.phoebus.pix.demo.utils.getListPixCards
import com.phoebus.pix.demo.viewmodels.FilterPixViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPixView(
    pixClient: PixClient,
    navController: NavController,
    isReport: Boolean,
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        content = {
            val viewModel: FilterPixViewModel = viewModel()
            FilterPixScreen(Modifier.padding(it), viewModel, navController, isReport, pixClient)
        }
    )
}

@Composable
fun FilterPixScreen(
    modifier: Modifier = Modifier,
    viewModel: FilterPixViewModel = viewModel(),
    navController: NavController,
    isReport: Boolean,
    pixClient: PixClient,
) {
    val selectedCard = viewModel.selectedCard.collectAsState().value
    val selected = viewModel.selectedFilter.value
    val context = LocalContext.current
    val errorMessage = viewModel.errorMessage.collectAsState().value

    var lastShownMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty() && errorMessage != lastShownMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        lastShownMessage = errorMessage
    }

    LaunchedEffect(viewModel.navigationEventHome) {
        viewModel.navigationEventHome.collect { event ->
            if(event){
                navController.navigate(Destinations.HOME.name) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(20.dp)) {
            PixCardsRow(selectedCard, viewModel)

            if (selectedCard == 1 || selectedCard == 3) {
                TimesPiecker(viewModel)
                if (selectedCard == 3) DatesPiecker(viewModel)
            }
            if(isReport){
                EnumFilterSelector(
                    selected = selected,
                    onSelectedChange = { viewModel.setSelectedFilter(it) }
                )
            }

            FilterButton(context, viewModel, navController, isReport, pixClient)
        }
    }
}

@Composable
fun PixCardsRow(selectedCard: Int, viewModel: FilterPixViewModel) {
    LazyRow(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        items(getListPixCards()) { item ->
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .size(100.dp)
                    .padding(5.dp),
                onClick = { viewModel.upgradeSelectedCard(item.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                enabled = true,
                colors = CardColors(
                    containerColor = if (item.id == selectedCard) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                    contentColor = if (item.id == selectedCard) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = item.icon, contentDescription = stringResource(item.text), modifier = Modifier.size(35.dp))
                    Text(text = stringResource(item.text), textAlign = TextAlign.Center, fontSize = 14.sp, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun FilterButton(context: Context, viewModel: FilterPixViewModel, navController: NavController, isReport: Boolean, pixClient: PixClient) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            modifier = Modifier.padding(15.dp),
            onClick = {
                val startDateTime = viewModel.getStartDateTimeForNavigation()
                val endDateTime = viewModel.getEndDateTimeForNavigation()
                if (!isReport){
                    if (DateUtils().validTime(viewModel.startTime.value, viewModel.endTime.value)) {
                        navController.navigate("${Destinations.LISTPIX.name}/$startDateTime/$endDateTime")
                    } else {
                        Toast.makeText(context, "Período inválido!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    try{
                        viewModel.getReport(pixClient)
                    }catch (e: Exception){
                        Log.e("Consult", "Error ao chamar o report", e)
                    }
                }
            }
        ) {
            Text(text = "Filtrar")
        }
    }
}

@Composable
fun EnumFilterSelector(
    selected: ReportType,
    onSelectedChange: (ReportType) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = ReportType.entries

    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = selected == option
            val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
            val textColor = if (isSelected) Color.White else Color.Black
            val reportText = when(option){
                ReportType.DETAILED -> stringResource(R.string.report_detailed)
                ReportType.SUMMARY -> stringResource(R.string.report_summary)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .clickable { onSelectedChange(option) }
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = reportText,
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}



@Composable
fun TimesPiecker(
    viewModel: FilterPixViewModel = viewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val startTime = viewModel.startTime.collectAsState()
        val endTime = viewModel.endTime.collectAsState()
        TimePickerText(time = startTime.value) {
            viewModel.upgradeStartTime(it)
        }
        Text("a", modifier = Modifier.padding(vertical = 0.dp, horizontal = 10.dp))
        TimePickerText(time = endTime.value) {
            viewModel.upgradeEndTime(it)
        }
    }
}

@Composable
fun DatesPiecker(
    viewModel: FilterPixViewModel = viewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val startDate = viewModel.startDate.collectAsState()
        val endDate = viewModel.endDate.collectAsState()
        DatePickerText(selectedDate = startDate.value) {
            viewModel.upgradeStartDate(it)
        }
        Text("a", modifier = Modifier.padding(vertical = 0.dp, horizontal = 10.dp))
        DatePickerText(selectedDate = endDate.value, minDate = startDate.value) {
            viewModel.upgradeEndDate(it)
        }
    }
}

@Composable
fun TimePickerText(
    modifier: Modifier = Modifier,
    time: String = "00:00",
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .clickable {
                openTimePicker(context, onTimeSelected)
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(color = Color.Black, thickness = 1.dp, modifier = Modifier.width(50.dp))
    }
}

@Composable
fun DatePickerText(
    modifier: Modifier = Modifier,
    selectedDate: String,
    minDate: String? = null,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = selectedDate,
            modifier = Modifier.clickable {
                openDatePicker(context, minDate, onDateSelected = onDateSelected)
            },
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(color = Color.Black, thickness = 1.dp, modifier = Modifier.width(90.dp))
    }
}

private fun openTimePicker(
    context: Context,
    onTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour,
        minute,
        true
    )

    timePicker.show()
}

private fun openDatePicker(
    context: Context,
    minDate: String? = null,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val formattedDate =
                String.format("%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)
        },
        year,
        month,
        dayOfMonth
    )
    if (minDate != null) {
        val parsedMinDate = DateUtils().stringDateToDate(minDate)
        datePicker.datePicker.minDate = parsedMinDate!!.time
    }
    val maxDate = calendar.timeInMillis
    datePicker.datePicker.maxDate = maxDate

    datePicker.show()
}

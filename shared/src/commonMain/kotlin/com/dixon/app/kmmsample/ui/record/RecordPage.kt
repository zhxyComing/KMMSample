package com.dixon.app.kmmsample.ui.record

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dixon.app.kmmsample.PayRecord
import com.dixon.app.kmmsample.core.base.Logger
import com.dixon.app.kmmsample.database.BasicData
import com.dixon.app.kmmsample.database.PayBasicDataTransfer
import com.dixon.app.kmmsample.database.PayRecordDataTransfer
import com.dixon.app.kmmsample.database.asBool
import com.dixon.app.kmmsample.database.tryObtainExpectAmount
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@Composable
fun RecordPage() {
    Column(modifier = Modifier.fillMaxSize().padding(top = 36.dp)) {
        DisplayMain()
    }
}

@Composable
fun DisplayMain() {
    var addRecordShow by remember { mutableStateOf(false) }
    val records = PayRecordDataTransfer.records
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addRecordShow = true
                },
                containerColor = Color.Red
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        containerColor = Color.Transparent
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            item { TargetCard() }
            items(count = records.size, key = {
                records[it].id
            }) {
                DetailCardItem(records[it])
            }
            item { BaseCard() }
        }
    }
    if (addRecordShow) {
        AddRecordDialog(
            onDismiss = { addRecordShow = false },
            onSaveClick = { timestamp, amount, income, desc ->
                Logger.i("RecordPage") { "AddRecordDialog click save: timestamp-$timestamp, amount-$amount, income-$income, desc-$desc" }
                PayRecordDataTransfer.insert(
                    timestamp = timestamp,
                    amount = amount,
                    income = income,
                    desc = desc
                )
            })
    }
}

@Composable
fun AddRecordDialog(
    onDismiss: () -> Unit,
    onSaveClick: (timestamp: Long, amount: Long, income: Boolean, desc: String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color(0XFFF0F0F0),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            var selectDate by remember { mutableStateOf(false) }
            var timestamp by remember { mutableStateOf(0L) }
            var amountInput by remember { mutableStateOf("") }
            var descInput by remember { mutableStateOf("") }
            var incomeFlag by remember { mutableStateOf(true) }
            if (!selectDate) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "新增记录",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Serif,
                        color = Color(0XFFCF6679),
                        fontSize = 24.sp
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (timestamp == 0L) "选择日期" else formatTimestamp(
                                timestamp
                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clickable {
                                    selectDate = true
                                }
                                .padding(top = 10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0XFFCF6679))
                                .padding(10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = if (incomeFlag) "收入" else "支出",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                        Checkbox(
                            checked = incomeFlag,
                            onCheckedChange = { incomeFlag = it },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }
                    AppTextField(
                        value = descInput,
                        onValueChange = { newText -> descInput = newText },
                        title = "记录描述",
                        titleSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    )
                    AppTextField(
                        value = amountInput,
                        onValueChange = { newText ->
                            if (newText.all { it.isDigit() }) {
                                amountInput = newText
                            }
                        },
                        title = "记录金额",
                        titleSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Button(
                        onClick = {
                            if (amountInput.isNotEmpty() && descInput.isNotEmpty() && timestamp != 0L) {
                                onSaveClick(
                                    timestamp,
                                    amountInput.toLong(),
                                    incomeFlag,
                                    descInput
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text("保存")
                    }
                }
            } else {
                // 日期选择器
                DatePicker(onSureClick = {
                    selectDate = false
                    timestamp = it
                })
            }
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    textStyle: TextStyle = TextStyle.Default,
    titleColor: Color = Color.Gray,
    titleSize: TextUnit = TextStyle.Default.fontSize,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Column {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(10.dp)
                        .height(20.dp)
                ) {
                    innerTextField() // This is where the text field is drawn
                }
            }
        }
    )
}

@Composable
fun AddBasicDialog(
    onDismiss: () -> Unit,
    onSaveClick: (timestamp: Long, amount: Long) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            var selectDate by remember { mutableStateOf(false) }
            var timestamp by remember { mutableStateOf(0L) }
            var amountInput by remember { mutableStateOf("") }
            if (!selectDate) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "基础信息设置",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Serif,
                        color = Color(0XFFCF6679),
                        fontSize = 24.sp
                    )
                    Text(
                        text = if (timestamp == 0L) "选择日期" else formatTimestamp(timestamp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable {
                                selectDate = true
                            }
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0XFFCF6679))
                            .padding(10.dp)
                    )
                    AppTextField(
                        value = amountInput,
                        onValueChange = { newText ->
                            if (newText.all { it.isDigit() }) {
                                amountInput = newText
                            }
                        },
                        title = "记录金额",
                        titleSize = 13.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Button(
                        onClick = {
                            if (amountInput.isNotEmpty() && timestamp != 0L) {
                                onSaveClick(timestamp, amountInput.toLong())
                                onDismiss()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text("保存")
                    }
                }
            } else {
                // 日期选择器
                DatePicker(onSureClick = {
                    selectDate = false
                    timestamp = it
                })
            }
        }
    }
}

@Composable
fun TargetCard() {
    val targetData by PayBasicDataTransfer.targetData
    val timeString = targetData?.let { formatTimestamp(it.timestamp) } ?: "长按设置"
    val amountString = (targetData?.amount ?: "长按设置").toString()
    var addBasicShow by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        addBasicShow = true
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Target Date",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0XFFCF6679),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = timeString,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "预期值：$amountString",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0XFFB2556A),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Bottom)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${tryObtainExpectAmount()}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }

    if (addBasicShow) {
        AddBasicDialog(
            onDismiss = { addBasicShow = false },
            onSaveClick = { timestamp, amount ->
                Logger.i("RecordPage") { "AddBasicDialog click save: timestamp-$timestamp, amount-$amount" }
                PayBasicDataTransfer.setTargetData(BasicData(timestamp, amount))
            })
    }
}

@Composable
fun DetailCard() {
    val records = PayRecordDataTransfer.records
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            count = records.size,
            key = {
                records[it].id
            }) {
            DetailCardItem(records[it])
        }
    }
}

@Composable
fun DetailCardItem(payRecord: PayRecord) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        // 长按事件
                        // TODO 增加弹窗确认
                        PayRecordDataTransfer.delete(payRecord)
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = formatTimestamp(payRecord.timestamp),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "${if (payRecord.income.asBool()) "+" else "-"}${payRecord.amount}",
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = if (payRecord.income.asBool()) Color(0XFFCF6679) else Color(0xFF03DAC6)
            )
            Text(
                modifier = Modifier.weight(1f),
                text = payRecord.desc,
                textAlign = TextAlign.End,
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun BaseCard() {
    val beginData by PayBasicDataTransfer.beginData
    val timeString = beginData?.let { formatTimestamp(it.timestamp) } ?: "长按设置"
    val amountString = (beginData?.amount ?: "长按设置").toString()
    var addBasicShow by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        addBasicShow = true
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Start Date",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF03DAC6),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = timeString,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp
                )
            }
            Text(
                text = "初始值：$amountString",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0XFF55B2A0),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
    if (addBasicShow) {
        AddBasicDialog(
            onDismiss = { addBasicShow = false },
            onSaveClick = { timestamp, amount ->
                Logger.i("RecordPage") { "AddBasicDialog click save: timestamp-$timestamp, amount-$amount" }
                PayBasicDataTransfer.setBeginData(BasicData(timestamp, amount))
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(onSureClick: (timestamp: Long) -> Unit) {
    val state = rememberDatePickerState()
    Column(modifier = Modifier.fillMaxWidth()) {
        DatePicker(state)
        val timestamp = state.selectedDateMillis
        if (timestamp != null) {
            Button(
                onClick = {
                    onSureClick.invoke(timestamp)
                },
                modifier = Modifier.padding(bottom = 30.dp).align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "确认选择 ${formatTimestamp(timestamp)}",
                )
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    // 将时间戳转换为 Instant
    val instant = Instant.fromEpochMilliseconds(timestamp)
    // 获取系统默认时区
    val timeZone = TimeZone.currentSystemDefault()
    // 转换为本地日期时间
    val localDateTime = instant.toLocalDateTime(timeZone)
    val dateFormat = LocalDateTime.Format {
        year()
        char('年')
        monthNumber()
        char('月')
        dayOfMonth()
        char('日')
    }
    // 格式化日期时间为字符串
    return localDateTime.format(dateFormat)
}
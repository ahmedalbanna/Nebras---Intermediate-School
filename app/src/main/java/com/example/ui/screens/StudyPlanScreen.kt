package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.StudyTask
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StudyPlanScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val availableSubjects = listOf(
        "رياضيات", "فيزياء", "كيمياء", "أحياء", "علوم عامة", "لغة عربية", "دراسات اجتماعية", "لغة إنجليزية"
    )

    val selectedSubjects = remember { mutableStateListOf("رياضيات", "فيزياء", "علوم عامة") }
    var selectedLevel by remember { mutableStateOf("متوسط - فاهم المعظم مع بعض الصعوبات") }
    var selectedPace by remember { mutableStateOf("معتدلة (30-45 دقيقة/جلسة)") }
    var selectedDailyHours by remember { mutableStateOf("2 - 3 ساعات يومياً") }
    var weakTopicsInput by remember { mutableStateOf("") }

    val levelOptions = listOf(
        "مبتدئ - يحتاج تأسيس وإعادة شرح المفاهيم",
        "متوسط - فاهم المعظم مع بعض الصعوبات",
        "متقدم - يهدف للمراجعة المكثفة وحل الامتحانات"
    )

    val paceOptions = listOf(
        "هادئة (20-30 دقيقة/جلسة)",
        "معتدلة (30-45 دقيقة/جلسة)",
        "سريعة ومكثفة (60 دقيقة/جلسة)"
    )

    val dailyHoursOptions = listOf(
        "1 - 2 ساعة يومياً",
        "2 - 3 ساعات يومياً",
        "3 - 5 ساعات يومياً"
    )

    val planUiState by viewModel.studyPlanState.collectAsState()
    val savedTasksFromDb by viewModel.dbStudyTasks.collectAsState()

    var showAddTaskDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<StudyTask?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مولد الخطط الدراسية المخصصة 🗓️", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Check if there is an active plan
            when (val state = planUiState) {
                is UiState.Success -> {
                    val tasks = state.data
                    val completedCount = tasks.count { it.isCompleted }
                    val totalCount = tasks.size
                    val progressRatio = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
                    val totalMinutes = tasks.sumOf { it.durationMinutes }

                    // Progress & Header Summary Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "جدولك الدراسي المخصص 🎯",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "$completedCount من $totalCount وحدات مكتملة • إجمالي ${totalMinutes / 60}س و ${totalMinutes % 60}د",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                    }

                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "${(progressRatio * 100).toInt()}%",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                LinearProgressIndicator(
                                    progress = { progressRatio },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surface
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { showAddTaskDialog = true },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("إضافة مهمة", style = MaterialTheme.typography.labelMedium)
                                    }

                                    OutlinedButton(
                                        onClick = { viewModel.saveCurrentPlanToDatabase(tasks) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("حفظ أوفلاين", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Group tasks by Day
                    val groupedTasks = tasks.groupBy { it.dayName }

                    groupedTasks.forEach { (dayName, dayTasks) ->
                        item {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = dayName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }

                        items(dayTasks, key = { it.id }) { task ->
                            StudyTaskCard(
                                task = task,
                                onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                                onEdit = { editingTask = task },
                                onDelete = { viewModel.deleteStudyTask(task.id) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = {
                                viewModel.generateStudyPlan(
                                    subjects = selectedSubjects.toList(),
                                    level = selectedLevel,
                                    pace = selectedPace,
                                    dailyHours = selectedDailyHours,
                                    weakTopics = weakTopicsInput
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("إعادة توليد خطة دراسية جديدة 🔄", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                is UiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "يقوم المستشار التربوي الذكي بصياغة الجدول والوحدات والتمارين والاختبارات...",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "يتم توزيع وقتك حسب المواد والسرعة التي اخترتها",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                else -> {
                    // Form Section to Configure Initial Study Plan
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "حدد معطيات الطالب لبناء الخطة المثالية 📋",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // 1. Subject Selection
                                Text(
                                    text = "1. المواد المراد تحسين الأداء والتفوق فيها:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    availableSubjects.forEach { subj ->
                                        val isSelected = selectedSubjects.contains(subj)
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable {
                                                if (isSelected) {
                                                    if (selectedSubjects.size > 1) selectedSubjects.remove(subj)
                                                } else {
                                                    selectedSubjects.add(subj)
                                                }
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (isSelected) {
                                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                }
                                                Text(
                                                    text = subj,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // 2. Knowledge Level
                                Text(
                                    text = "2. مستوى المعرفة الحالي للطالب:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                levelOptions.forEach { lvl ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedLevel = lvl }
                                    ) {
                                        RadioButton(
                                            selected = selectedLevel == lvl,
                                            onClick = { selectedLevel = lvl }
                                        )
                                        Text(
                                            text = lvl,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // 3. Preferred Learning Pace
                                Text(
                                    text = "3. سرعة التعلم وجلسات المذاكرة المفضلة:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                paceOptions.forEach { pace ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedPace = pace }
                                    ) {
                                        RadioButton(
                                            selected = selectedPace == pace,
                                            onClick = { selectedPace = pace }
                                        )
                                        Text(
                                            text = pace,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // 4. Daily Hours Available
                                Text(
                                    text = "4. الوقت اليومي المتاح للمذاكرة:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    dailyHoursOptions.forEach { hours ->
                                        val isSelected = hours == selectedDailyHours
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.clickable { selectedDailyHours = hours }
                                        ) {
                                            Text(
                                                text = hours,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // 5. Weak topics input
                                Text(
                                    text = "5. مواضيع أو قوانين صعبة ترغب في التركيز عليها (اختياري):",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = weakTopicsInput,
                                    onValueChange = { weakTopicsInput = it },
                                    placeholder = { Text("مثال: مسائل الحركة والعجلة، إعراب الأفعال، التفاعلات الكيميائية...") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        viewModel.generateStudyPlan(
                                            subjects = selectedSubjects.toList(),
                                            level = selectedLevel,
                                            pace = selectedPace,
                                            dailyHours = selectedDailyHours,
                                            weakTopics = weakTopicsInput
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("إنشاء الخطة الدراسية بالذكاء الاصطناعي ✨", fontWeight = FontWeight.Bold)
                                }

                                if (savedTasksFromDb.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { viewModel.loadSavedPlanFromDatabase() },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.FolderOpen, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("تحميل الخطة المحفوظة في الجهاز 📂", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Custom Task Dialog
    if (showAddTaskDialog) {
        AddOrEditTaskDialog(
            taskToEdit = null,
            onDismiss = { showAddTaskDialog = false },
            onSave = { newTask ->
                viewModel.addCustomStudyTask(newTask)
                showAddTaskDialog = false
            }
        )
    }

    // Edit Existing Task Dialog
    editingTask?.let { task ->
        AddOrEditTaskDialog(
            taskToEdit = task,
            onDismiss = { editingTask = null },
            onSave = { updated ->
                viewModel.updateStudyTask(updated)
                editingTask = null
            }
        )
    }
}

@Composable
fun StudyTaskCard(
    task: StudyTask,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val typeBadgeColor = when (task.taskType) {
        "EXERCISE" -> MaterialTheme.colorScheme.secondary
        "QUIZ" -> Color(0xFF8B5CF6)
        else -> MaterialTheme.colorScheme.primary
    }

    val typeLabel = when (task.taskType) {
        "EXERCISE" -> "✏️ تمارين وتطبيقات"
        "QUIZ" -> "❓ اختبار تقييمي"
        else -> "📖 وحدة دراسية"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleComplete),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = typeBadgeColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = typeLabel,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = typeBadgeColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = task.subject,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${task.durationMinutes} دقيقة",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "تعديل",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddOrEditTaskDialog(
    taskToEdit: StudyTask?,
    onDismiss: () -> Unit,
    onSave: (StudyTask) -> Unit
) {
    var dayName by remember { mutableStateOf(taskToEdit?.dayName ?: "اليوم 1") }
    var subject by remember { mutableStateOf(taskToEdit?.subject ?: "رياضيات") }
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var taskType by remember { mutableStateOf(taskToEdit?.taskType ?: "STUDY") }
    var durationMinutesText by remember { mutableStateOf(taskToEdit?.durationMinutes?.toString() ?: "30") }
    var description by remember { mutableStateOf(taskToEdit?.description ?: "") }

    val taskTypes = listOf("STUDY" to "📖 وحدة دراسية", "EXERCISE" to "✏️ تمارين وتطبيقات", "QUIZ" to "❓ اختبار تقييمي")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (taskToEdit == null) "إضافة مهمة دراسية جديدة ➕" else "تعديل المهمة الدراسية ✏️") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = dayName,
                    onValueChange = { dayName = it },
                    label = { Text("اليوم في الجدول (مثال: اليوم 1)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("المادة الدراسية") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان الدرس / المهمة") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("نوع المهمة:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    taskTypes.forEach { (typeKey, label) ->
                        val isSel = taskType == typeKey
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { taskType = typeKey }
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = durationMinutesText,
                    onValueChange = { durationMinutesText = it.filter { char -> char.isDigit() } },
                    label = { Text("المدة بالدقائق") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("تفاصيل أو نصيحة دراسية") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val duration = durationMinutesText.toIntOrNull() ?: 30
                        val updatedOrNew = taskToEdit?.copy(
                            dayName = dayName,
                            subject = subject,
                            title = title,
                            taskType = taskType,
                            durationMinutes = duration,
                            description = description
                        ) ?: StudyTask(
                            dayName = dayName,
                            subject = subject,
                            title = title,
                            taskType = taskType,
                            durationMinutes = duration,
                            description = description
                        )
                        onSave(updatedOrNew)
                    }
                }
            ) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

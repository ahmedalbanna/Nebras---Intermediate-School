package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.FormattedOutputCard
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConceptExplainScreen(
    viewModel: MainViewModel,
    initialSubject: String = "علوم عامة",
    onBack: () -> Unit
) {
    var selectedSubject by remember { mutableStateOf(initialSubject) }
    var conceptInput by remember { mutableStateOf("") }
    var simplificationLevel by remember { mutableStateOf("بسيط مع تشبيهات") }

    val subjects = listOf("علوم عامة", "فيزياء", "كيمياء", "أحياء", "رياضيات", "لغة عربية", "دراسات اجتماعية", "لغة إنجليزية")
    val levels = listOf("بسيط مع تشبيهات", "مبسط جداً للأشخاص الصغار", "مختصر وسريع")

    val sampleTopics = mapOf(
        "علوم عامة" to listOf("الخلية الحية", "التركيب الضوئي", "حالات المادة الثلاث", "الذرة ومكوناتها"),
        "فيزياء" to listOf("قوانين نيوتن للحركة", "السرعة والتسارع", "الضغط الهيدروليكي", "الكهرباء الساكنة"),
        "كيمياء" to listOf("التفاعل الكيميائي", "الجدول الدوري", "الأحماض والقواعد", "الروابط الكيميائية"),
        "أحياء" to listOf("الجهاز الهضمي", "الوراثة والجينات", "الدورة الدموية", "النظام البيئي"),
        "رياضيات" to listOf("المعادلات الخطية", "نظرية فيثاغورس", "النسبة المئوية", "الاحتمالات والاحصاء"),
        "لغة عربية" to listOf("إن وأخواتها", "الفاعل والمفعول به", "الميزان الصرفي", "البلاغة والتشبيه"),
        "دراسات اجتماعية" to listOf("خطوط الطول ودوائر العرض", "المناخ والعوامل المؤثرة", "المكتبة التاريخية"),
        "لغة إنجليزية" to listOf("Present Simple vs Continuous", "Passive Voice", "Conditionals (If)")
    )

    val conceptUiState by viewModel.conceptState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("شرح المفاهيم المبسط 💡", fontWeight = FontWeight.Bold) },
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
            // Subject Selection Header
            item {
                Text(
                    text = "اختر المادة الدراسية:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(subjects) { subject ->
                        val isSelected = subject == selectedSubject
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedSubject = subject }
                        ) {
                            Text(
                                text = subject,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Input Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ما المفهوم الذي تريد فهمه؟ 🤔",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = conceptInput,
                            onValueChange = { conceptInput = it },
                            placeholder = { Text("مثال: قانون نيوتن الأول، التفاعل الكيميائي، إن وأخواتها...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Sample Topics
                        val currentSamples = sampleTopics[selectedSubject] ?: emptyList()
                        if (currentSamples.isNotEmpty()) {
                            Text(
                                text = "مواضيع مقترحة للتجربة:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(currentSamples) { topic ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                        modifier = Modifier.clickable { conceptInput = topic }
                                    ) {
                                        Text(
                                            text = topic,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Simplification level selection
                        Text(
                            text = "أسلوب الشرح والتبسيط:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            levels.forEach { level ->
                                val isSelected = level == simplificationLevel
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.clickable { simplificationLevel = level }
                                ) {
                                    Text(
                                        text = level,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.explainConcept(conceptInput, selectedSubject, simplificationLevel)
                            },
                            enabled = conceptInput.isNotBlank() && conceptUiState !is UiState.Loading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (conceptUiState is UiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("جاري إعداد الشرح المبسط...")
                            } else {
                                Icon(Icons.Default.Lightbulb, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("اشرح لي المفهوم بوضوح 💡", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Result State Display
            item {
                when (val state = conceptUiState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("يقوم المعلم الذكي بصياغة الشرح والتشبيهات المناسبة...")
                            }
                        }
                    }
                    is UiState.Error -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    is UiState.Success -> {
                        FormattedOutputCard(
                            rawText = state.data,
                            title = "الشرح المبسط لـ: $conceptInput",
                            subject = selectedSubject,
                            onSaveToLibrary = {
                                viewModel.saveResultToLibrary(
                                    title = conceptInput,
                                    subject = selectedSubject,
                                    itemType = "CONCEPT",
                                    inputContent = conceptInput,
                                    outputContent = state.data
                                )
                            }
                        )
                    }
                    UiState.Idle -> {
                        // Empty state tip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 ادخل اسم الدرس أو المفهوم واضغط على 'اشرح لي المفهوم' لتحصل على تعريف واسع ومثال تشبيهي لا يُنسى!",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Book
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
fun LessonSummarizerScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedSubject by remember { mutableStateOf("علوم عامة") }
    var lessonContent by remember { mutableStateOf("") }

    val subjects = listOf("علوم عامة", "دراسات اجتماعية", "لغة عربية", "فيزياء", "كيمياء", "أحياء", "لغة إنجليزية")

    val sampleLessons = mapOf(
        "علوم عامة" to "يتكون النظام الشمسي من الشمس وثمانية كواكب تدور حولها في مدارات بيضاوية. تقسم الكواكب إلى كواكب صخرية داخلية وهي عطارد والزهرة والأرض والمريخ، وكواكب غازية عملاقة خارجية وهي المشتري وزحل وأورانوس ونبتون. تتميز الأرض بوجود غلاف جوي وماء سائل مما يسمح بالحياة.",
        "دراسات اجتماعية" to "تعتبر الحضارة المصرية القديمة من أعرق الحضارات الإنسانية. قامت على ضفاف نهر النيل الذي وفر المياه والتربة الخصبة للزراعة. ابتكر المصريون القدماء الكتابة الهيروغليفية وبنوا الأهرامات والمعابد وشيدوا نظام ري متطور.",
        "لغة عربية" to "تعد الجملة الاسمية هي الجملة التي تبدأ باسم، وتتكون من ركنين أساسيين هما المبتدأ والخبر. المبتدأ هو الاسم المتحدث عنه، والخبر هو الجزء الذي يتمم معنى الجملة ويكمل الفائدة. وكلاهما مرفوع بالضمة في حالة المفرد."
    )

    val summaryUiState by viewModel.summaryState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("تلخيص الدروس والبطاقات 📝", fontWeight = FontWeight.Bold) },
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
            // Subject Row
            item {
                Text("المادة:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(subjects) { subject ->
                        val isSelected = subject == selectedSubject
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedSubject = subject }
                        ) {
                            Text(
                                text = subject,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
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
                            text = "ألصق أو اكتب نص الدرس هنا:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = lessonContent,
                            onValueChange = { lessonContent = it },
                            placeholder = { Text("اكتب فقرة من الدرس للتلخيص والترتيب...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Sample quick text button
                        val sample = sampleLessons[selectedSubject]
                        if (sample != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.clickable { lessonContent = sample }
                            ) {
                                Text(
                                    text = "💡 استخدام نص نمذجي تجريبي لمادة $selectedSubject",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.summarizeLesson(lessonContent, selectedSubject) },
                            enabled = lessonContent.isNotBlank() && summaryUiState !is UiState.Loading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (summaryUiState is UiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("جاري تلخيص وتنسيق النقاط والبطاقات...")
                            } else {
                                Icon(Icons.Default.Book, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("لخص الدرس ورتب النقاط 📝", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Results Display
            item {
                when (val state = summaryUiState) {
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
                                Text("يقوم المساعد بتلخيص المفاهيم الرئيسية وإنشاء بطاقات المراجعة...")
                            }
                        }
                    }
                    is UiState.Error -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
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
                            title = "ملخص الدرس الذكي",
                            subject = selectedSubject,
                            onSaveToLibrary = {
                                viewModel.saveResultToLibrary(
                                    title = "ملخص $selectedSubject: ${lessonContent.take(25)}...",
                                    subject = selectedSubject,
                                    itemType = "SUMMARY",
                                    inputContent = lessonContent,
                                    outputContent = state.data
                                )
                            }
                        )
                    }
                    UiState.Idle -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📝 يساعدك التلخيص الذكي على قراءة الدروس الطويلة في دقائق، ويستخرج النقاط المهمة والمصطلحات الرئيسية مع بطاقات سؤال وجواب لتختبر نفسك بها!",
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

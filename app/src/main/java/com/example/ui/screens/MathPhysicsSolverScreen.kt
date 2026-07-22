package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.FormattedOutputCard
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathPhysicsSolverScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedSubject by remember { mutableStateOf("فيزياء") }
    var problemInput by remember { mutableStateOf("") }

    val subjects = listOf("فيزياء", "رياضيات", "كيمياء")

    val sampleProblems = mapOf(
        "فيزياء" to listOf(
            "سيارة تحركت بسرعة 60 كم/ساعة لمسافة 120 كم، أحسب الزمن المستغرق بالدقائق؟",
            "جسم كتلته 5 كجم يتحرك بعجلة 3 م/ث²، أحسب القوة المؤثرة عليه وفق قانون نيوتن الثاني؟",
            "احسب كثافة مكعب خشبي كتلته 200 جرام وحجمه 250 سم³؟"
        ),
        "رياضيات" to listOf(
            "حل المعادلة التالية: 2س + 5 = 15 أوجد قيمة س مع توضيح خطوات النقل والتبسيط؟",
            "مثلث قائم الزاوية طول ضلعيه القائمين 6 سم و 8 سم، احسب طول الوتر باستخدام نظرية فيثاغورس؟",
            "اشترى طالب كتاباً بخصم 20% وكان سعره الأصلي 50 ريال، احسب السعر بعد الخصم؟"
        ),
        "كيمياء" to listOf(
            "احسب عدد مولات 40 جرام من غاز هيدروكسيد الصوديوم NaOH علماً بأن الكتلة المولية له 40 ج/مول؟",
            "وضح بالخطوات كيف تتوازن المعادلة الكيميائية: H2 + O2 -> H2O"
        )
    )

    val solverUiState by viewModel.mathSolverState.collectAsState()
    val practiceUiState by viewModel.practiceProblemState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("حلال المسائل وخطوات الحل 🎯", fontWeight = FontWeight.Bold) },
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
            // Top Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.height(110.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.img_math_banner),
                            contentDescription = "الرياضيات والفيزياء",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Subject Selector
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("المادة:", fontWeight = FontWeight.Bold)
                    subjects.forEach { subj ->
                        val isSelected = subj == selectedSubject
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { selectedSubject = subj }
                        ) {
                            Text(
                                text = subj,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
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
                            text = "اكتب نص المسألة أو الرياضية/الفيزيائية:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = problemInput,
                            onValueChange = { problemInput = it },
                            placeholder = { Text("مثال: احسب السرعة إذا كانت المسافة 100م والزمن 20 ثانية...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Samples
                        val samples = sampleProblems[selectedSubject] ?: emptyList()
                        if (samples.isNotEmpty()) {
                            Text(
                                text = "أمثلة سريعة للمسائل:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            samples.forEach { sample ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                        .clickable { problemInput = sample }
                                ) {
                                    Text(
                                        text = sample,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.solveMathPhysics(problemInput, selectedSubject) },
                            enabled = problemInput.isNotBlank() && solverUiState !is UiState.Loading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (solverUiState is UiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("جاري تحليل وفك المسألة واستخراج القانون...")
                            } else {
                                Icon(Icons.Default.Calculate, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("حل واشرح خطوات الطريق 🎯", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Solution Output
            item {
                when (val state = solverUiState) {
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("يعمل المساعد الذكي على تحضير الخطوات والنصائح العلمية...")
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
                        Column {
                            FormattedOutputCard(
                                rawText = state.data,
                                title = "خطوات حل المسألة",
                                subject = selectedSubject,
                                onSaveToLibrary = {
                                    viewModel.saveResultToLibrary(
                                        title = problemInput.take(30) + "...",
                                        subject = selectedSubject,
                                        itemType = "MATH_SOLVER",
                                        inputContent = problemInput,
                                        outputContent = state.data
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action to generate a similar practice problem
                            OutlinedButton(
                                onClick = {
                                    viewModel.generatePracticeProblem(problemInput, selectedSubject)
                                },
                                enabled = practiceUiState !is UiState.Loading,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                if (practiceUiState is UiState.Loading) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("جاري إنشاء مسألة تدريبية مشابهة...")
                                } else {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("ولّد مسألة تدريبية مشابهة لترسيخ الفهم 🔄", fontWeight = FontWeight.Bold)
                                }
                            }

                            // Display Generated Practice Problem
                            when (val pState = practiceUiState) {
                                is UiState.Success -> {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    FormattedOutputCard(
                                        rawText = pState.data,
                                        title = "مسألة تدريبية واختبار سريـع",
                                        subject = selectedSubject
                                    )
                                }
                                is UiState.Error -> {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = pState.message,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                    UiState.Idle -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎯 هذا القسم يركز على 'فهم طريقة الحل': يتم عرض القانون المستعمل، معطيات المسألة، الحل خطوة بخطوة، ونصيحة لتجنب الخطأ الشائع في الامتحان!",
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

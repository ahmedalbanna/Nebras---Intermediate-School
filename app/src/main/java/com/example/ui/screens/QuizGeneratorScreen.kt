package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.InteractiveQuiz
import com.example.data.model.QuizQuestion
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizGeneratorScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedSubject by remember { mutableStateOf("علوم عامة") }
    var topicInput by remember { mutableStateOf("") }

    val subjects = listOf("علوم عامة", "رياضيات", "فيزياء", "كيمياء", "أحياء", "لغة عربية", "دراسات اجتماعية", "لغة إنجليزية")

    val interactiveQuizState by viewModel.interactiveQuizState.collectAsState()

    val preDesignedQuizzes = remember {
        listOf(
            InteractiveQuiz(
                title = "قوانين الحركة والسرعة 🚀",
                subject = "فيزياء",
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        question = "سيارة تقطع مسافة 150 كيلومتر خلال 3 ساعات. كم تكون سرعتها المتوسطة؟",
                        options = listOf("50 كم/س", "450 كم/س", "150 كم/س", "30 كم/س"),
                        correctAnswerIndex = 0,
                        explanation = "💡 التغذية الراجعة: السرعة = المسافة ÷ الزمن = 150 ÷ 3 = 50 كم/س.\n⚠️ خطأ شائع: ضرب المسافة في الزمن بدلاً من القسمة عليهم."
                    ),
                    QuizQuestion(
                        id = 2,
                        question = "ما هي وحدة قياس العجلة (التسارع) في النظام الدولي للوحدات؟",
                        options = listOf("متر / ثانية (م/ث)", "متر / ثانية² (م/ث²)", "كيلوجرام (كجم)", "نيوتن (ن)"),
                        correctAnswerIndex = 1,
                        explanation = "💡 التغذية الراجعة: م/ث² هي وحدة التسارع لأنها تقيس معدل التغير في السرعة لكل ثانية.\n⚠️ خطأ شائع: الخلط بين وحدة السرعة (م/ث) ووحدة التسارع (م/ث²)."
                    ),
                    QuizQuestion(
                        id = 3,
                        question = "ينص قانون نيوتن الأول للحركة على أن الجسم الساكن يبقى ساكناً ما لم تؤثر عليه:",
                        options = listOf("طاقة حرارية", "قوة محصلة خارجية", "كتلة إضافية", "سرعة منتظمة"),
                        correctAnswerIndex = 1,
                        explanation = "💡 التغذية الراجعة: القوة المحصلة الخارجية هي التي تُحدث تغييراً في الحالة الحركية للجسم (قانون القصور الذاتي)."
                    )
                )
            ),
            InteractiveQuiz(
                title = "إنَّ وأخواتها وكان وأخواتها ✍️",
                subject = "لغة عربية",
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        question = "عند دخول الحرف النافخ (إنَّ) على الجملة الاسمية (الطالبُ مجتهدٌ)، تصبح:",
                        options = listOf("إنَّ الطالبُ مجتهدٌ", "إنَّ الطالبَ مجتهدٌ", "إنَّ الطالبَ مجتهداً", "إنَّ الطالبُ مجتهداً"),
                        correctAnswerIndex = 1,
                        explanation = "💡 التغذية الراجعة: (إنَّ) تنصب المبتدأ فيسمى اسمها (الطالبَ) وترفع الخبر فيسمى خبرها (مجتهدٌ).\n⚠️ خطأ شائع: نسيان أن خبر إنَّ ينبغي أن يظل مرفوعاً بالضمة."
                    ),
                    QuizQuestion(
                        id = 2,
                        question = "ما هو العمل النحوي لـ (كان) وأخواتها في الجملة الاسمية؟",
                        options = listOf("تنصب المبتدأ وترفع الخبر", "ترفع المبتدأ وتنصب الخبر", "تنصب المبتدأ والخبر معاً", "ترفع المبتدأ والخبر معاً"),
                        correctAnswerIndex = 1,
                        explanation = "💡 التغذية الراجعة: كان وأخواتها أفعال ناسخة ترفع المبتدأ ويسمى اسمها، وتنصب الخبر ويسمى خبرها (عكس عمل إنَّ وأخواتها)."
                    ),
                    QuizQuestion(
                        id = 3,
                        question = "في جملة (صار الجوُّ بارداً)، يعرب كلمة (بارداً):",
                        options = listOf("اسم صار مرفوع", "خبر صار منصوب", "فاعل مرفوع", "مفعول به منصوب"),
                        correctAnswerIndex = 1,
                        explanation = "💡 التغذية الراجعة: (صار) من أخوات كان، والجوُّ اسمها مرفوع، و(بارداً) خبر صار منصوب وعلامة نصبه الفتحة."
                    )
                )
            ),
            InteractiveQuiz(
                title = "تركيب الذرة والجدول الدوري ⚛️",
                subject = "كيمياء",
                questions = listOf(
                    QuizQuestion(
                        id = 1,
                        question = "الجسيمات سالبة الشحنة التي تدور حول نواة الذرة هي:",
                        options = listOf("البروتونات", "النيوترونات", "الإلكترونات", "الفوتونات"),
                        correctAnswerIndex = 2,
                        explanation = "💡 التغذية الراجعة: الإلكترونات هي الجسيمات السالبة التي تدور في مستويات الطاقة حول النواة.\n⚠️ خطأ شائع: خلط شحنة البروتونات (الموجبة) مع شحنة الإلكترونات (السالبة)."
                    ),
                    QuizQuestion(
                        id = 2,
                        question = "تتكون نواة الذرة في المركز من:",
                        options = listOf("إلكترونات ونيوترونات", "بروتونات ونيوترونات", "إلكترونات فقط", "بروتونات وإلكترونات"),
                        correctAnswerIndex = 1,
                        explanation = "💡 التغذية الراجعة: تحتوي النواة على البروتونات الموجبة والنيوترونات المتعادلة كهرائياً."
                    ),
                    QuizQuestion(
                        id = 3,
                        question = "العدد الذري لأي عنصر كيميائي يساوي عدد:",
                        options = listOf("النيوترونات فقط", "البروتونات في النواة", "الإلكترونات والنيوترونات معاً", "مستويات الطاقة"),
                        correctAnswerIndex = 1,
                        explanation = "💡 التغذية الراجعة: العدد الذري = عدد البروتونات الموجبة داخل النواة (ويساوي عدد الإلكترونات في الذرة المتعادلة)."
                    )
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مولد ومُختَبِر الفهم التفاعلي ❓", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (interactiveQuizState is UiState.Success) {
                            viewModel.resetQuizState()
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = interactiveQuizState) {
                is UiState.Success -> {
                    // Active Interactive Quiz View
                    InteractiveQuizPlayer(
                        quiz = state.data,
                        onReset = { viewModel.resetQuizState() },
                        onSaveToLibrary = {
                            viewModel.saveResultToLibrary(
                                title = "اختبار تفاعلي: ${state.data.title}",
                                subject = state.data.subject,
                                itemType = "QUIZ",
                                inputContent = state.data.title,
                                outputContent = state.data.questions.joinToString("\n\n") { q ->
                                    "س: ${q.question}\nالإجابة الصحيحة: ${q.options.getOrNull(q.correctAnswerIndex)}\nتفسير: ${q.explanation}"
                                }
                            )
                        }
                    )
                }

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "يقوم المعلم الذكي بإعداد 5 أسئلة تفاعلية مع التغذية الراجعة وتوضيح الأخطاء الشائعة...",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                else -> {
                    // Quiz Creation Form & Pre-designed Quizzes
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        // Pre-designed Quick Quizzes Header
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "اختبارات تفاعلية قصيرة جاهزة للمراجعة ⚡",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(preDesignedQuizzes) { quiz ->
                                    Card(
                                        modifier = Modifier
                                            .width(220.dp)
                                            .clickable { viewModel.loadPreDesignedQuiz(quiz) },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.secondary
                                            ) {
                                                Text(
                                                    text = quiz.subject,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = quiz.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("ابدأ الاختبار الآن", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))
                        }

                        // Custom Topic Quiz Generator Form
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "ولّد اختبار فهم في أي درس بالذكاء الاصطناعي 🎯",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("اختر المادة الدراسية:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text("اسم الدرس أو الموضوع المراد اختباره:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(6.dp))

                                    OutlinedTextField(
                                        value = topicInput,
                                        onValueChange = { topicInput = it },
                                        placeholder = { Text("مثال: التفاعلات الكيميائية، إعراب الأفعال، معادلات الحركة...") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = { viewModel.generateQuiz(topicInput, selectedSubject) },
                                        enabled = topicInput.isNotBlank(),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("إنشاء الاختبار التفاعلي ❓", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveQuizPlayer(
    quiz: InteractiveQuiz,
    onReset: () -> Unit,
    onSaveToLibrary: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() } // QuestionIndex -> SelectedOptionIndex
    var isQuizSubmitted by remember { mutableStateOf(false) }

    val totalQuestions = quiz.questions.size
    val currentQuestion = quiz.questions.getOrNull(currentQuestionIndex)

    if (isQuizSubmitted) {
        // Quiz Results & Breakdown Screen
        val correctCount = quiz.questions.indices.count { idx ->
            userAnswers[idx] == quiz.questions[idx].correctAnswerIndex
        }
        val scorePercent = ((correctCount.toFloat() / totalQuestions) * 100).toInt()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "نتيجة اختبار الفهم 🏆",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "$scorePercent%",
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "أجبت بشكل صحيح على $correctCount من أصل $totalQuestions أسئلة",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onSaveToLibrary,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("حفظ النتائج")
                            }

                            OutlinedButton(
                                onClick = {
                                    userAnswers.clear()
                                    currentQuestionIndex = 0
                                    isQuizSubmitted = false
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إعادة الاختبار")
                            }

                            OutlinedButton(
                                onClick = onReset,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("اختبار جديد")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "مراجعة الأسئلة والتغذية الراجعة للأخطاء الشائعة 📝:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(quiz.questions.indices.toList()) { idx ->
                val q = quiz.questions[idx]
                val selectedOpt = userAnswers[idx]
                val isCorrect = selectedOpt == q.correctAnswerIndex

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isCorrect) Color(0xFF16A34A) else MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "سؤال ${idx + 1}: ${q.question}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "إجابتك: ${selectedOpt?.let { q.options.getOrNull(it) } ?: "لم يتم الإجابة"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCorrect) Color(0xFF16A34A) else MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )

                        if (!isCorrect) {
                            Text(
                                text = "الإجابة الصحيحة: ${q.options.getOrNull(q.correctAnswerIndex)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF16A34A),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = q.explanation,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    } else if (currentQuestion != null) {
        // Active Question Screen
        val selectedOptionForCurrent = userAnswers[currentQuestionIndex]
        val hasAnsweredCurrent = selectedOptionForCurrent != null

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Quiz Progress Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "السؤال ${currentQuestionIndex + 1} من $totalQuestions",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = quiz.subject,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Options List
                    currentQuestion.options.forEachIndexed { optIndex, optionText ->
                        val isSelected = selectedOptionForCurrent == optIndex
                        val isCorrectOption = optIndex == currentQuestion.correctAnswerIndex

                        val backgroundColor = when {
                            !hasAnsweredCurrent -> if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            isCorrectOption -> Color(0xFFDCFCE7) // Light Green
                            isSelected -> Color(0xFFFEE2E2) // Light Red
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        }

                        val borderColor = when {
                            !hasAnsweredCurrent -> if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            isCorrectOption -> Color(0xFF16A34A)
                            isSelected -> MaterialTheme.colorScheme.error
                            else -> Color.Transparent
                        }

                        val textColor = when {
                            !hasAnsweredCurrent -> MaterialTheme.colorScheme.onSurface
                            isCorrectOption -> Color(0xFF15803D)
                            isSelected -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = backgroundColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                .clickable(enabled = !hasAnsweredCurrent) {
                                    userAnswers[currentQuestionIndex] = optIndex
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (hasAnsweredCurrent && isCorrectOption) Color(0xFF16A34A) else if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = ('أ' + optIndex).toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected || (hasAnsweredCurrent && isCorrectOption)) FontWeight.Bold else FontWeight.Normal,
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )

                                if (hasAnsweredCurrent && isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF16A34A),
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (hasAnsweredCurrent && isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Immediate Feedback Card (Visible once answered)
                    AnimatedVisibility(
                        visible = hasAnsweredCurrent,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "التغذية الراجعة وشرح الأخطاء الشائعة 💡",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentQuestion.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentQuestionIndex > 0) currentQuestionIndex--
                    },
                    enabled = currentQuestionIndex > 0,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("السؤال السابق")
                }

                Button(
                    onClick = {
                        if (currentQuestionIndex < totalQuestions - 1) {
                            currentQuestionIndex++
                        } else {
                            isQuizSubmitted = true
                        }
                    },
                    enabled = hasAnsweredCurrent,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(if (currentQuestionIndex < totalQuestions - 1) "السؤال التالي ⬅️" else "عرض النتيجة 🏆")
                }
            }
        }
    }
}

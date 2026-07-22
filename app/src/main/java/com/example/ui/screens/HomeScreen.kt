package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

import androidx.compose.material.icons.filled.DateRange

@Composable
fun HomeScreen(
    onNavigateToConcept: (subject: String) -> Unit,
    onNavigateToMathSolver: () -> Unit,
    onNavigateToSummarizer: () -> Unit,
    onNavigateToEssayCorrector: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToStudyPlan: () -> Unit
) {
    val subjects = listOf(
        "علوم عامة", "رياضيات", "فيزياء", "كيمياء", "أحياء", "لغة عربية", "دراسات اجتماعية", "لغة إنجليزية"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Header Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = "نبراس التعليمي",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xCC0F172A)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondary
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "المساعد الذكي للطلاب",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "نبراس التعليمي للمرحلة المتوسطة",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "بناء أساس علمي قوي • فهم الطريقة والخطوات • إتقان مهارات الدراسة",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Subject Selector Chips
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(
                    text = "اختر المادة الدراسية للمساعدة 📚",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(subjects) { subject ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.clickable { onNavigateToConcept(subject) }
                        ) {
                            Text(
                                text = subject,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // Main Tools Grid Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "أدوات الذكاء الاصطناعي التعليمية 🛠️",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Card 0: Study Plan Generator
                ToolCardItem(
                    title = "تخصيص الخطة والجدول الدراسي 🗓️",
                    subtitle = "جدولة ذكية مخصصة حسب مستواك وسرعة تعلمك والمواد التي ترغب بتمكينها",
                    icon = Icons.Default.DateRange,
                    badgeText = "جدولك المخصص",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = onNavigateToStudyPlan
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Card 1: Concept Explainer
                ToolCardItem(
                    title = "شرح المفاهيم المعقدة 💡",
                    subtitle = "تبسيط العلوم والأدب والمصطلحات الصعبة مع تشبيهات رائعة",
                    icon = Icons.Default.Lightbulb,
                    badgeText = "شرح وتبسيط",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = { onNavigateToConcept("علوم عامة") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Card 2: Math/Physics Solver
                ToolCardItem(
                    title = "حلال المسائل (خطوة بخطوة) 🎯",
                    subtitle = "الرياضيات والفيزياء والكيمياء - التركيز على فهم الطريقة وتجنب الأخطاء",
                    icon = Icons.Default.Calculate,
                    badgeText = "فهم الطريقة",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    onClick = onNavigateToMathSolver
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Card 3: Summarizer
                ToolCardItem(
                    title = "تلخيص الدروس والبطاقات 📝",
                    subtitle = "استخراج الأفكار الرئيسية والمفاهيم وتوليد بطاقات مراجعة سريعة",
                    icon = Icons.Default.Book,
                    badgeText = "ملخصات ذكية",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    iconTint = MaterialTheme.colorScheme.tertiary,
                    onClick = onNavigateToSummarizer
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Card 4: Essay Corrector
                ToolCardItem(
                    title = "تصحيح التعبير والكتابة ✍️",
                    subtitle = "تصحيح الإملاء والنحو واقتراح تحسينات ومفردات بلاغية راقية",
                    icon = Icons.Default.Create,
                    badgeText = "تطوير الكتابة",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    iconTint = Color(0xFF10B981),
                    onClick = onNavigateToEssayCorrector
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Card 5: Quiz Generator
                ToolCardItem(
                    title = "اختبار الفهم التفاعلي ❓",
                    subtitle = "مولد أسئلة اختيار من متعدد في أي موضوع مع تفسير الإجابات",
                    icon = Icons.Default.Quiz,
                    badgeText = "اختبر نفسك",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    iconTint = Color(0xFF8B5CF6),
                    onClick = onNavigateToQuiz
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Card 6: Saved Library
                ToolCardItem(
                    title = "المكتبة والمحفوظات 📚",
                    subtitle = "جميع المفاهيم والملخصات والمسائل المحفوظة للمراجعة بدون إنترنت",
                    icon = Icons.Default.Bookmark,
                    badgeText = "مذاكرة أوفلاين",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    iconTint = MaterialTheme.colorScheme.primary,
                    onClick = onNavigateToSaved
                )
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Pedagogy Note Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "💡 سر النجاح في المرحلة المتوسطة:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "لا تكتفِ بالحصول على الإجابة الجاهزة، بل ركز دائماً على فهم الخطوة والسبب العلمي وراءها لتبني أساساً متيناً للثانوية!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToolCardItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconTint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = iconTint.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = badgeText,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = iconTint,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

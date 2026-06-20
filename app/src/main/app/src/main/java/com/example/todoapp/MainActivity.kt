**Zone noire (le contenu) :** Colle tout ton code Kotlin Compose :
   ```kotlin
   package com.example.todoapp

   import android.os.Bundle
   import androidx.activity.ComponentActivity
   import androidx.activity.compose.setContent
   import androidx.compose.foundation.background
   import androidx.compose.foundation.layout.*
   import androidx.compose.foundation.lazy.LazyColumn
   import androidx.compose.foundation.lazy.items
   import androidx.compose.foundation.shape.RoundedCornerShape
   import androidx.compose.material.icons.Icons
   import androidx.compose.material.icons.filled.Add
   import androidx.compose.material.icons.filled.Delete
   import androidx.compose.material3.*
   import androidx.compose.runtime.*
   import androidx.compose.ui.Alignment
   import androidx.compose.ui.Modifier
   import androidx.compose.ui.graphics.Color
   import androidx.compose.ui.text.font.FontWeight
   import androidx.compose.ui.unit.dp
   import androidx.compose.ui.unit.sp

   class MainActivity : ComponentActivity() {
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContent {
               TodoApp()
           }
       }
   }

   @Composable
   fun TodoApp() {
       var tasks by remember { mutableStateOf(listOf<Task>()) }
       var inputValue by remember { mutableStateOf("") }

       Column(
           modifier = Modifier
               .fillMaxSize()
               .background(Color(0xFFF5F5F5))
               .padding(16.dp)
       ) {
           Text(
               text = "📋 Ma Todo List",
               fontSize = 28.sp,
               fontWeight = FontWeight.Bold,
               color = Color(0xFF333333),
               modifier = Modifier.padding(bottom = 20.dp)
           )

           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .background(Color.White, RoundedCornerShape(12.dp))
                   .padding(12.dp),
               verticalAlignment = Alignment.CenterVertically
           ) {
               TextField(
                   value = inputValue,
                   onValueChange = { inputValue = it },
                   placeholder = { Text("Ajoute une tâche...") },
                   modifier = Modifier
                       .weight(1f)
                       .background(Color.Transparent),
                   singleLine = true
               )
            
               Button(
                   onClick = {
                       if (inputValue.isNotBlank()) {
                           tasks = tasks + Task(
                               id = System.currentTimeMillis(),
                               title = inputValue,
                               isCompleted = false
                           )
                           inputValue = ""
                       }
                   },
                   modifier = Modifier.padding(start = 8.dp),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = Color(0xFF6200EE)
                   )
               ) {
                   Icon(Icons.Filled.Add, contentDescription = "Ajouter")
               }
           }

           Spacer(modifier = Modifier.height(20.dp))

           if (tasks.isEmpty()) {
               Box(
                   modifier = Modifier
                       .fillMaxSize()
                       .padding(32.dp),
                   contentAlignment = Alignment.Center
               ) {
                   Text(
                       text = "Aucune tâche pour le moment 🎉",
                       fontSize = 16.sp,
                       color = Color.Gray
                   )
               }
           } else {
               LazyColumn(
                   verticalArrangement = Arrangement.spacedBy(12.dp)
               ) {
                   items(tasks) { task ->
                       TaskItem(
                           task = task,
                           onToggle = { 
                               tasks = tasks.map {
                                   if (it.id == task.id) it.copy(isCompleted = !it.isCompleted)
                                   else it
                               }
                           },
                           onDelete = {
                               tasks = tasks.filter { it.id != task.id }
                           }
                       )
                   }
               }
           }

           Spacer(modifier = Modifier.height(20.dp))
           Text(
               text = "✅ ${tasks.count { it.isCompleted }} / ${tasks.size} complétées",
               fontSize = 14.sp,
               color = Color.Gray,
               modifier = Modifier.align(Alignment.CenterHorizontally)
           )
       }
   }

   @Composable
   fun TaskItem(
       task: Task,
       onToggle: () -> Unit,
       onDelete: () -> Unit
   ) {
       Row(
           modifier = Modifier
               .fillMaxWidth()
               .background(Color.White, RoundedCornerShape(8.dp))
               .padding(12.dp),
           verticalAlignment = Alignment.CenterVertically
       ) {
           Checkbox(
               checked = task.isCompleted,
               onCheckedChange = { onToggle() },
               modifier = Modifier.padding(end = 12.dp)
           )

           Text(
               text = task.title,
               modifier = Modifier
                   .weight(1f)
                   .padding(8.dp),
               fontSize = 16.sp,
               color = if (task.isCompleted) Color.Gray else Color.Black
           )

           IconButton(onClick = onDelete) {
               Icon(
                   Icons.Filled.Delete,
                   contentDescription = "Supprimer",
                   tint = Color.Red
               )
           }
       }
   }

   data class Task(
       val id: Long,
       val title: String,
       val isCompleted: Boolean
   )

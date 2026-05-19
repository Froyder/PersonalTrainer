package com.froyder.personaltrainer.data.remote

import com.froyder.personaltrainer.data.model.User

fun buildPlanPrompt(user: User): String = """
You are a professional fitness coach. Generate a weekly workout plan for this user.

USER PROFILE:
- Name: ${user.name}
- Age: ${user.age}
- Weight: ${user.weightKg} kg
- Height: ${user.heightCm} cm
- Goal: ${user.goal}
- Fitness Level: ${user.level}
- Available Equipment: ${user.equipment}
- Training Days Per Week: ${user.daysPerWeek}

INSTRUCTIONS:
- Generate exactly ${user.daysPerWeek} training days
- Each day must have 4-6 exercises appropriate for the user's level and equipment
- Estimate realistic session duration in minutes
- Assign rest time between sets in seconds
- Each exercise must include a "description" field with 3-4 sentences explaining proper form, technique, common mistakes to avoid, and any helpful tips for beginners
- Each exercise must include a "youtubeQuery" field with a short search query (5-8 words) that would find a good tutorial video on YouTube

Respond ONLY with a valid JSON object, no markdown, no explanation. Use this exact structure:
{
  "weeklyDays": [
    {
      "id": "day_1",
      "dayLabel": "Monday",
      "focusLabel": "Upper Body",
      "estimatedMinutes": 45,
      "isCompleted": false,
      "exercises": [
        {
          "id": "ex_1",
          "name": "Push-ups",
          "muscleGroup": "CHEST",
          "difficulty": "MEDIUM",
          "sets": 3,
          "reps": 12,
          "restSeconds": 60,
          "notes": "",
          "description": "Start in a high plank position with hands shoulder-width apart. Lower your chest to the floor keeping your body straight, then push back up.",
          "youtubeQuery": "push-ups proper form tutorial for beginners"
        }
      ]
    }
  ]
}

muscleGroup must be one of: CHEST, BACK, SHOULDERS, ARMS, LEGS, CORE, FULL_BODY, CARDIO
difficulty must be one of: EASY, MEDIUM, HARD
""".trimIndent()
import 'package:flutter/material.dart';

class StudentDashboardPage extends StatefulWidget {
  @override
  _StudentDashboardPageState createState() => _StudentDashboardPageState();
}

class _StudentDashboardPageState extends State<StudentDashboardPage> {
  // Exemple de données de cours et d'exercices
  List<Map<String, dynamic>> courses = [
    {
      'title': 'Cours de Mathématiques',
      'type': 'Vidéo',
      'description': 'Introduction aux équations différentielles.',
      'isRealTime': false,
    },
    {
      'title': 'Cours de Physique',
      'type': 'Fiche de cours',
      'description': 'Les lois de la thermodynamique.',
      'isRealTime': false,
    },
    {
      'title': 'Cours d\'Histoire en direct',
      'type': 'Temps réel',
      'description': 'L’histoire contemporaine (années 1800 à nos jours).',
      'isRealTime': true,
    },
  ];

  List<Map<String, dynamic>> exercises = [
    {
      'title': 'QCM de Mathématiques',
      'type': 'QCM',
      'description': 'Révision sur les équations quadratiques.',
    },
    {
      'title': 'Exercice de Physique',
      'type': 'Questions-Réponses',
      'description': 'Exercice sur les lois de Newton.',
    },
  ];

  List<String> recommendations = [
    'Réviser les leçons de géométrie pour améliorer la compréhension des angles.',
    'Participer aux cours en direct pour une meilleure interaction avec les enseignants.',
  ];

  List<String> activityHistory = [
    'Cours de Mathématiques - Terminé',
    'Exercice de Physique - En cours',
    'Cours d\'Histoire en direct - À venir',
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Tableau de bord des élèves'),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Section des cours
              Text(
                'Cours disponibles',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              SizedBox(height: 10),
              ...courses.map((course) {
                return Card(
                  child: ListTile(
                    title: Text(course['title']),
                    subtitle: Text(course['description']),
                    trailing: Text(course['type']),
                    onTap: () {
                      _openCourse(course);
                    },
                  ),
                );
              }).toList(),
              SizedBox(height: 20),

              // Section des exercices
              Text(
                'Exercices',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              SizedBox(height: 10),
              ...exercises.map((exercise) {
                return Card(
                  child: ListTile(
                    title: Text(exercise['title']),
                    subtitle: Text(exercise['description']),
                    trailing: Text(exercise['type']),
                    onTap: () {
                      _openExercise(exercise);
                    },
                  ),
                );
              }).toList(),
              SizedBox(height: 20),

              // Section des recommandations
              Text(
                'Recommandations pour vous',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              SizedBox(height: 10),
              ...recommendations.map((rec) {
                return ListTile(
                  leading: Icon(Icons.recommend),
                  title: Text(rec),
                );
              }).toList(),
              SizedBox(height: 20),

              // Section de l'historique des activités
              Text(
                'Historique des activités',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              SizedBox(height: 10),
              ...activityHistory.map((activity) {
                return ListTile(
                  leading: Icon(Icons.history),
                  title: Text(activity),
                );
              }).toList(),
            ],
          ),
        ),
      ),
    );
  }

  // Ouvrir le cours sélectionné
  void _openCourse(Map<String, dynamic> course) {
    if (course['isRealTime']) {
      // Logique pour les cours en temps réel
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text('Cours en direct'),
            content: Text('Le cours en direct commencera à l\'heure prévue.'),
            actions: <Widget>[
              ElevatedButton(
                child: Text('OK'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        },
      );
    } else {
      // Logique pour les autres cours (vidéos, fiches de cours)
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => CourseDetailPage(course: course)),
      );
    }
  }

  // Ouvrir l'exercice sélectionné
  void _openExercise(Map<String, dynamic> exercise) {
    // Logique pour les exercices (QCM, Questions-Réponses)
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => ExerciseDetailPage(exercise: exercise)),
    );
  }
}

// Exemple de page de détail pour un cours (à implémenter)
class CourseDetailPage extends StatelessWidget {
  final Map<String, dynamic> course;

  CourseDetailPage({required this.course});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(course['title']),
      ),
      body: Center(
        child: Text('Détails du cours: ${course['description']}'),
      ),
    );
  }
}

// Exemple de page de détail pour un exercice (à implémenter)
class ExerciseDetailPage extends StatelessWidget {
  final Map<String, dynamic> exercise;

  ExerciseDetailPage({required this.exercise});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(exercise['title']),
      ),
      body: Center(
        child: Text('Détails de l\'exercice: ${exercise['description']}'),
      ),
    );
  }
}

import 'package:flutter/material.dart';

class ParentDashboardPage extends StatefulWidget {
  @override
  _ParentDashboardPageState createState() => _ParentDashboardPageState();
}

class _ParentDashboardPageState extends State<ParentDashboardPage> {
  // Exemple de données à afficher (à remplacer par des données réelles)
  List<Map<String, dynamic>> students = [
    {
      'name': 'Alice Dupont',
      'lastLogin': '24/09/2024 14:30',
      'coursesCompleted': 5,
      'activitiesDuration': '3h 45min',
      'progress': 75,
      'recommendations': [
        'Réviser les mathématiques pour améliorer la note.',
        'Pratiquer plus souvent la lecture.'
      ]
    },
    {
      'name': 'Lucas Martin',
      'lastLogin': '23/09/2024 18:15',
      'coursesCompleted': 3,
      'activitiesDuration': '2h 10min',
      'progress': 60,
      'recommendations': [
        'Travailler sur les exercices de sciences.',
        'Améliorer la gestion du temps de révision.'
      ]
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Tableau de bord parents'),
      ),
      body: ListView.builder(
        itemCount: students.length,
        itemBuilder: (context, index) {
          final student = students[index];
          return Card(
            margin: EdgeInsets.all(16.0),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Élève: ${student['name']}',
                    style: TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  SizedBox(height: 10),
                  Text('Dernière connexion: ${student['lastLogin']}'),
                  Text('Cours terminés: ${student['coursesCompleted']}'),
                  Text('Durée d\'activité: ${student['activitiesDuration']}'),
                  SizedBox(height: 10),
                  Text('Progression: ${student['progress']}%'),
                  LinearProgressIndicator(
                    value: student['progress'] / 100,
                    backgroundColor: Colors.grey[300],
                    color: Colors.blue,
                  ),
                  SizedBox(height: 10),
                  Text('Recommandations:'),
                  for (var rec in student['recommendations'])
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 4.0),
                      child: Text('- $rec'),
                    ),
                  SizedBox(height: 10),
                  ElevatedButton(
                    onPressed: () {
                      _setReminder(student['name']);
                    },
                    child: Text('Définir un rappel'),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  // Fonction pour définir un rappel
  void _setReminder(String studentName) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Définir un rappel'),
          content: Text('Définir un rappel pour que $studentName revienne sur l\'application.'),
          actions: <Widget>[
            ElevatedButton(
              child: Text('Annuler'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            ElevatedButton(
              child: Text('Confirmer'),
              onPressed: () {
                Navigator.of(context).pop();
                // Logique pour définir le rappel (Notification, etc.)
              },
            ),
          ],
        );
      },
    );
  }
}

import 'package:flutter/material.dart';

// Importation des deux pages d'inscription
import 'ParentRegistrationPage.dart';
import 'StudentRegistrationPage.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Firestore',
      home: RegistrationTogglePage(),
    );
  }
}

class RegistrationTogglePage extends StatefulWidget {
  @override
  _RegistrationTogglePageState createState() => _RegistrationTogglePageState();
}

class _RegistrationTogglePageState extends State<RegistrationTogglePage> {
  // Variable pour suivre l'état du toggle (true = Parent, false = Élève)
  bool isParent = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Inscription - Cours en Ligne'),
      ),
      body: Column(
        children: <Widget>[
          // Toggle Switch pour changer entre l'inscription Parent et Élève
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text('Inscription Élève'),
                Switch(
                  value: isParent,
                  onChanged: (value) {
                    setState(() {
                      isParent = value;
                    });
                  },
                ),
                Text('Inscription Parent'),
              ],
            ),
          ),
          // Affichage de la page d'inscription en fonction de la valeur du toggle
          Expanded(
            child: isParent ? ParentRegistrationPage() : StudentRegistrationPage(),
          ),
        ],
      ),
    );
  }
}

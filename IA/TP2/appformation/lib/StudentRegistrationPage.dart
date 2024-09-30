import 'package:appformation/StudentDashboardPage.dart';
import 'package:flutter/material.dart';

class StudentRegistrationPage extends StatefulWidget {
  @override
  _StudentRegistrationPageState createState() => _StudentRegistrationPageState();
}

class _StudentRegistrationPageState extends State<StudentRegistrationPage> {
  final _formKey = GlobalKey<FormState>();
  String? studentFirstName, studentLastName, email, username, password;
  String? schoolLevel;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Inscription Élève'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: <Widget>[
              // Nom de l'élève
              TextFormField(
                decoration: InputDecoration(labelText: 'Nom'),
                onSaved: (value) => studentLastName = value,
                validator: (value) {
                  if (value!.isEmpty) {
                    return 'Veuillez entrer un nom.';
                  }
                  return null;
                },
              ),
              // Prénom de l'élève
              TextFormField(
                decoration: InputDecoration(labelText: 'Prénom'),
                onSaved: (value) => studentFirstName = value,
                validator: (value) {
                  if (value!.isEmpty) {
                    return 'Veuillez entrer un prénom.';
                  }
                  return null;
                },
              ),
              // Login
              TextFormField(
                decoration: InputDecoration(labelText: 'Nom d\'utilisateur'),
                onSaved: (value) => username = value,
                validator: (value) {
                  if (value!.isEmpty) {
                    return 'Veuillez entrer un nom d\'utilisateur.';
                  }
                  return null;
                },
              ),
              // Email
              TextFormField(
                decoration: InputDecoration(labelText: 'Adresse mail'),
                onSaved: (value) => email = value,
                validator: (value) {
                  if (value!.isEmpty || !value.contains('@')) {
                    return 'Veuillez entrer une adresse mail valide.';
                  }
                  return null;
                },
                keyboardType: TextInputType.emailAddress,
              ),
              // Mot de passe
              TextFormField(
                decoration: InputDecoration(labelText: 'Mot de passe'),
                onSaved: (value) => password = value,
                validator: (value) {
                  if (value!.length < 6) {
                    return 'Le mot de passe doit contenir au moins 6 caractères.';
                  }
                  return null;
                },
                obscureText: true,
              ),
              // Niveau scolaire
              DropdownButtonFormField<String>(
                decoration: InputDecoration(labelText: 'Niveau scolaire'),
                items: ['Primaire', 'Collège', 'Lycée', 'Université']
                    .map((level) => DropdownMenuItem(
                  value: level,
                  child: Text(level),
                ))
                    .toList(),
                onChanged: (value) {
                  setState(() {
                    schoolLevel = value;
                  });
                },
                validator: (value) {
                  if (value == null) {
                    return 'Veuillez sélectionner un niveau scolaire.';
                  }
                  return null;
                },
              ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: _submitForm,
                child: Text('S\'inscrire'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _submitForm() {
    if (_formKey.currentState!.validate()) {
      _formKey.currentState!.save();
      // Logique d'envoi du formulaire
      print('Inscription réussie');
      print('Nom: $studentLastName');
      print('Prénom: $studentFirstName');
      print('Email: $email');
      print('Niveau scolaire: $schoolLevel');
      // Envoyer un email de vérification et autres étapes
      _onLoginSuccess();
    }
  }
  void _onLoginSuccess() {
    // Logique de connexion réussie (après validation des identifiants)
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(builder: (context) => StudentDashboardPage()),
    );
  }
}

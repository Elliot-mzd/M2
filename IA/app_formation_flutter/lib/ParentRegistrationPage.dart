import 'package:flutter/material.dart';

import 'ParentDashboardPage.dart';

class ParentRegistrationPage extends StatefulWidget {
  @override
  _ParentRegistrationPageState createState() => _ParentRegistrationPageState();
}

class _ParentRegistrationPageState extends State<ParentRegistrationPage> {
  final _formKey = GlobalKey<FormState>();
  String? parentFirstName, parentLastName, email, username, password;
  String? twitter, facebook;
  int? numberOfChildren;

  List<Map<String, String>> childrenProfiles = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Inscription Parent'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: ListView(
            children: <Widget>[
              // Nom du parent
              TextFormField(
                decoration: InputDecoration(labelText: 'Nom'),
                onSaved: (value) => parentLastName = value,
                validator: (value) {
                  if (value!.isEmpty) {
                    return 'Veuillez entrer un nom.';
                  }
                  return null;
                },
              ),
              // Prénom du parent
              TextFormField(
                decoration: InputDecoration(labelText: 'Prénom'),
                onSaved: (value) => parentFirstName = value,
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
              // Twitter
              TextFormField(
                decoration: InputDecoration(labelText: 'Compte Twitter'),
                onSaved: (value) => twitter = value,
              ),
              // Facebook
              TextFormField(
                decoration: InputDecoration(labelText: 'Compte Facebook'),
                onSaved: (value) => facebook = value,
              ),
              // Nombre d'enfants
              TextFormField(
                decoration: InputDecoration(labelText: 'Nombre d\'élèves'),
                onSaved: (value) => numberOfChildren = int.tryParse(value!),
                keyboardType: TextInputType.number,
                validator: (value) {
                  if (value == null || int.tryParse(value) == null) {
                    return 'Veuillez entrer un nombre valide d\'élèves.';
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
      print('Nom: $parentLastName');
      print('Prénom: $parentFirstName');
      print('Email: $email');
      // Envoyer un email d'activation et autres étapes
      _onLoginSuccess();
    }
  }
  void _onLoginSuccess() {
    // Logique de connexion réussie (après validation des identifiants)
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(builder: (context) => ParentDashboardPage()),
    );
  }
}

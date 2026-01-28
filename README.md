# ZSWebsite

## Project: Personal Portfolio & AI Content Generator

This website is a full-stack application I designed and built from the ground up to serve as my dynamic professional portfolio. More than a static site, it features a secure, multi-tier cloud architecture and an integrated AI assistant.

## Architectural Overview

The application is built on a distributed, multi-tier architecture designed for security and scalability:

# Frontend (DMZ): The client-facing interface (built with Angular/Typescript) is hosted on GCP, serving as a Demilitarized Zone (DMZ) to securely isolate user traffic from the core backend infrastructure. Fly.io is used only for rerouting.
# Backend API: The business logic is handled by a Spring Boot application running on Google Cloud Platform (GCP). It exposes a set of RESTful APIs to manage data and power the site's dynamic features.
# Database: A PostgreSQL database, managed via the Supabase cloud service, provides a reliable and scalable persistence layer.

## Key Features

AI-Powered Content Generation: A standout feature is a custom-built AI assistant. It leverages my CV, skills, and project descriptions to automatically generate tailored documents like cover letters, application letters, and motivational statements.
# Secure, Decoupled Design: The multi-tier architecture with a proxy layer enhances security and allows each component to be developed, deployed, and scaled independently.
Automated DevOps Pipeline: The entire project is managed with a CI/CD pipeline using GitHub Actions, automating builds, testing, and deployments in a containerized Docker environment.

## Technology Stack

Backend: Spring Boot, Java, Hibernate (ORM)
Frontend: Angular / React
Database: PostgreSQL, Liquibase (for version control)
DevOps & Cloud: Docker, GitHub Actions (CI/CD), Fly.io, Google Cloud Platform (GCP), Supabase

# Utiliser PHP 8.3 avec Apache
FROM php:8.3-apache

# Installer les dépendances système et les extensions PHP pour Symfony
RUN apt-get update && apt-get install -y \
    libicu-dev \
    libpq-dev \
    libzip-dev \
    zip \
    unzip \
    git \
    && docker-php-ext-install intl pdo pdo_pgsql zip \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Activer le module Apache Rewrite
RUN a2enmod rewrite

# Pointer le serveur Apache vers le dossier /public de Symfony
ENV APACHE_DOCUMENT_ROOT /var/www/html/public
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/sites-available/000-default.conf
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/apache2.conf

# Activer AllowOverride pour le .htaccess
RUN sed -ri -e 's!AllowOverride None!AllowOverride All!g' /etc/apache2/apache2.conf

# Installer Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Définir le répertoire de travail
WORKDIR /var/www/html

# Copier les fichiers du projet
COPY . .

# Variables d'environnement pour le build
ENV APP_ENV=prod
ENV APP_DEBUG=0

# Installer les dépendances
RUN composer install --no-dev --optimize-autoloader --no-scripts

# Créer les dossiers et donner les droits à Apache
RUN mkdir -p var/cache var/log && \
    chown -R www-data:www-data var/

# ⚠️ PAS DE cache:clear ICI - Le cache se génère automatiquement

# Exposer le port 80
EXPOSE 80

CMD ["apache2-foreground"]
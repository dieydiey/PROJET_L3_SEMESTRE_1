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
    && docker-php-ext-install intl pdo pdo_pgsql zip

# Activer le module Apache Rewrite pour Symfony (gestion des URLs)
RUN a2enmod rewrite

# Pointer le serveur Apache vers le dossier /public de Symfony
ENV APACHE_DOCUMENT_ROOT /var/www/html/public
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/sites-available/000-default.conf
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/apache2.conf

# Installer Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Définir le répertoire de travail
WORKDIR /var/www/html

# Copier les fichiers du projet
COPY . .

# --- FIX POUR L'ERREUR 255 ---
# Définir les variables d'environnement pour le build
ENV APP_ENV=prod
ENV APP_DEBUG=0

# 1. Installer sans scripts pour ne pas charger DebugBundle
RUN composer install --no-dev --optimize-autoloader --no-scripts

# 2. Créer les dossiers nécessaires
RUN mkdir -p var/cache var/log

# 3. Générer le cache de production manuellement
RUN php bin/console cache:clear --env=prod

# 4. Donner les droits à Apache
RUN chown -R www-data:www-data var/
# -----------------------------

# Exposer le port 80
EXPOSE 80

CMD ["apache2-foreground"]

FROM php:8.3-apache

# Installer dépendances système
RUN apt-get update && apt-get install -y \
    git unzip libicu-dev libonig-dev libzip-dev \
    && docker-php-ext-install intl pdo pdo_mysql zip

# Activer mod_rewrite
RUN a2enmod rewrite

# Dossier de travail
WORKDIR /var/www/html

# Copier le projet Symfony
COPY . .

# Installer Composer
COPY --from=composer:2 /usr/bin/composer /usr/bin/composer

# Installer les dépendances Symfony
RUN composer install --no-dev --optimize-autoloader

# Permissions
RUN chown -R www-data:www-data /var/www/html

# Apache écoute sur 8080 (Render)
EXPOSE 8080
RUN sed -i 's/80/8080/g' /etc/apache2/ports.conf \
 && sed -i 's/:80/:8080/g' /etc/apache2/sites-enabled/000-default.conf

# Lancer Apache
CMD ["apache2-foreground"]

FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

# Copier le dossier du projet
COPY brasilBurger/ ./brasilBurger/

# Aller dans le dossier du projet
WORKDIR /src/brasilBurger

# Restaurer et publier
RUN dotnet restore
RUN dotnet publish -c Release -o /app/publish

FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS final
WORKDIR /app
EXPOSE 8080

COPY --from=build /app/publish .
ENTRYPOINT ["dotnet", "brasilBurger.dll"]

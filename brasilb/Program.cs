using BrasilBurger.Data;
using BrasilBurger.Repositories.Interfaces;
using BrasilBurger.Repositories.Impl;  // ✅ .Impl et non .Implementations
using BrasilBurger.Services.Interfaces;
using BrasilBurger.Services.Impl;      // ✅ .Impl et non .Implementations
using Npgsql;
using BrasilBurger.Models;



var builder = WebApplication.CreateBuilder(args);

var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");

var dataSourceBuilder = new NpgsqlDataSourceBuilder(connectionString);

// 🔥 Mapping ENUM PostgreSQL <-> C#
dataSourceBuilder.MapEnum<RoleUtilisateur>("role_utilisateur");
dataSourceBuilder.MapEnum<TypeComplement>("type_complement");
dataSourceBuilder.MapEnum<ModeConsommation>("mode_consommation");
dataSourceBuilder.MapEnum<EtatCommande>("etat_commande");
dataSourceBuilder.MapEnum<MethodePaiement>("methode_paiement");
dataSourceBuilder.MapEnum<StatutPaiement>("statut_paiement");

// IMPORTANT
dataSourceBuilder.EnableUnmappedTypes();

var dataSource = dataSourceBuilder.Build();

// Injection du DataSource
builder.Services.AddSingleton(dataSource);


// Enregistrer DatabaseHelper (singleton)
builder.Services.AddSingleton<DatabaseConfig>();

// Repositories
builder.Services.AddScoped<IBurgerRepository, BurgerRepository>();
builder.Services.AddScoped<IMenuRepository, MenuRepository>();
builder.Services.AddScoped<IComplementRepository, ComplementRepository>();
builder.Services.AddScoped<ICommandeRepository, CommandeRepository>();
builder.Services.AddScoped<IUtilisateurRepository, UtilisateurRepository>();
builder.Services.AddScoped<IPaiementRepository, PaiementRepository>();
builder.Services.AddScoped<IZoneRepository, ZoneRepository>();

// Services
builder.Services.AddScoped<IBurgerService, BurgerService>();
builder.Services.AddScoped<IMenuService, MenuService>();
builder.Services.AddScoped<IComplementService, ComplementService>();
builder.Services.AddScoped<ICommandeService, CommandeService>();
builder.Services.AddScoped<IAuthService, AuthService>();
builder.Services.AddScoped<IPaiementService, PaiementService>();
builder.Services.AddScoped<IZoneService, ZoneService>();

// Session pour le panier
builder.Services.AddDistributedMemoryCache();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

// MVC
builder.Services.AddControllersWithViews();
builder.Services.AddHttpContextAccessor();

var app = builder.Build();

// Configure le pipeline HTTP
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseSession();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Catalogue}/{action=Index}/{id?}");

app.Run();
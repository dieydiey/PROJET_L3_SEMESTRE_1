var builder = WebApplication.CreateBuilder(args);

// SERVICES
builder.Services.AddControllersWithViews();

// PORT RENDER
builder.WebHost.UseUrls("http://+:8080");

var app = builder.Build();

// PIPELINE
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
}

app.UseRouting();

app.UseAuthorization();

// ROUTES MVC
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();

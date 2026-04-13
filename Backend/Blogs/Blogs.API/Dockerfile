FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app
EXPOSE 80

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

COPY ["Blogs.API/Blogs.API.csproj", "Blogs.API/"]
COPY ["Blogs.Core/Blogs.Core.csproj", "Blogs.Core/"]
COPY ["Blogs.Infrastructure/Blogs.Infrastructure.csproj", "Blogs.Infrastructure/"]

RUN dotnet restore "Blogs.API/Blogs.API.csproj"

COPY . .
WORKDIR "/src/Blogs.API"
RUN dotnet build "Blogs.API.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "Blogs.API.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Blogs.API.dll"]
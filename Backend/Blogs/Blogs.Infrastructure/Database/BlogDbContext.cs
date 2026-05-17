using Blogs.Core.Model;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;

namespace Blogs.Infrastructure.Database
{
    public class BlogDbContext : DbContext
    {
        public DbSet<Blog> Blogs { get; set; }
        public DbSet<Like> Likes { get; set; }
        public DbSet<Comment> Comments { get; set; }

        public BlogDbContext(DbContextOptions<BlogDbContext> options) : base(options)
        {
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Blog>(entity =>
            {
                entity.HasKey(b => b.Id);
                entity.Property(b => b.Title).IsRequired();
                entity.Property(b => b.Description).IsRequired();
                entity.Property(b => b.Images)
                    .HasConversion(
                        v => string.Join(",", v),
                        v => v.Split(",", System.StringSplitOptions.RemoveEmptyEntries).ToList()
                    )
                    .Metadata.SetValueComparer(new ValueComparer<List<string>>(
                        (c1, c2) => c1.SequenceEqual(c2),
                        c => c.Aggregate(0, (a, v) => HashCode.Combine(a, v.GetHashCode())),
                        c => c.ToList()
                    ));
            });

            modelBuilder.Entity<Like>(entity =>
            {
                entity.HasKey(l => l.Id);

                entity.Property(l => l.UserId)
                    .IsRequired();

                entity.Property(l => l.BlogId)
                    .IsRequired();
            });

            modelBuilder.Entity<Comment>(entity =>
            {
                entity.HasKey(c => c.Id);

                entity.Property(c => c.UserId)
                    .IsRequired();

                entity.Property(c => c.BlogId)
                    .IsRequired();

                entity.Property(c => c.Content)
                    .IsRequired();

                entity.Property(c => c.CreatedAt)
                    .IsRequired();

                entity.Property(c => c.LastChange)
                    .IsRequired();
            });


        }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using Blogs.Core.Interfaces;
using Blogs.Core.Model;
using Blogs.Infrastructure.Database;

namespace Blogs.Infrastructure.Repositories
{
    public class BlogRepository : IBlogRepository
    {
        private readonly BlogDbContext _context;

        public BlogRepository(BlogDbContext context)
        {
            _context = context;
        }

        public Blog Create(Blog blog)
        {
            _context.Blogs.Add(blog);
            _context.SaveChanges();
            return blog;
        }

        public Blog GetById(Guid id)
        {
            return _context.Blogs.FirstOrDefault(b => b.Id == id);
        }

        public List<Blog> GetAll()
        {
            return _context.Blogs.ToList();
        }
    }
}
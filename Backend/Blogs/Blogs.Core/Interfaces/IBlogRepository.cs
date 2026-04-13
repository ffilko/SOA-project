using System;
using System.Collections.Generic;
using Blogs.Core.Model;

namespace Blogs.Core.Interfaces
{
    public interface IBlogRepository
    {
        Blog Create(Blog blog);
        Blog GetById(Guid id);
        List<Blog> GetAll();
    }
}
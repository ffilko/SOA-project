using System;
using System.Collections.Generic;

namespace Blogs.Core.Model
{
    public class Blog
    {
        public Guid Id { get; set; }
        public Guid UserId { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public DateTime CreatedAt { get; set; }
        public List<string> Images { get; set; }

        public Blog()
        {
            Id = Guid.NewGuid();
            CreatedAt = DateTime.UtcNow;
            Images = new List<string>();
        }
    }
}
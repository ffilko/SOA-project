using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.Model
{
    public class Like
    {
        public Guid Id { get; set; }
        public Guid UserId { get; set; }
        public Guid BlogId { get; set; }

        public Like()
        {
            Id = Guid.NewGuid();
        }

    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.DTO
{
    public class CreateLikeDTO
    {
        public Guid UserId { get; set; }
        public Guid BlogId { get; set; }
    }
}

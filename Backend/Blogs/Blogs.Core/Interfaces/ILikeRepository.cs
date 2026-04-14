using Blogs.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blogs.Core.Interfaces
{
    public interface ILikeRepository
    {
        Like Create(Like like);
        void Delete(Like like);
        Like GetById(Guid id);
        List<Like> GetAll();
    }
}

#ifndef CMEMORY_HPP
#define CMEMORY_HPP

#include <cstddef>

namespace cmemory {

    void* malloc(size_t size);
    void free(void* address);
    void* realloc(void* oldAddress, size_t size);

}

#endif //CMEMORY_HPP

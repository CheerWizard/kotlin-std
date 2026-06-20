#include "cmemory.hpp"
#include <cstdlib>

namespace cmemory {

    void* malloc(size_t size) {
        return ::malloc(size);
    }

    void free(void* address) {
        ::free(address);
    }

    void* realloc(void* oldAddress, size_t size) {
        return ::realloc(oldAddress, size);
    }

}
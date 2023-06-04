function createLocalStorage() {
  const DEFAULT_TTL = 1000 * 60 * 60 * 24 * 7
  const set = (key, value, ttl = DEFAULT_TTL) => {
    localStorage.setItem(key, JSON.stringify({ value, ttl: Date.now() + ttl }))
  }

  const remove = (key) => {
    localStorage.removeItem(key)
  }

  const get = (key) => {
    const item = localStorage.getItem(key)
    if (item) {
      const { value, ttl } = JSON.parse(item)
      if (!ttl || Date.now() <= ttl)
        return value
      remove(key)
      return undefined
    }
    return undefined
  }

  const clear = () => {
    localStorage.clear()
  }

  return {
    set,
    get,
    remove,
    clear,
  }
}

export const useLocalStorage = createLocalStorage()

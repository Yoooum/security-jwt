function createSessionStorage() {
  const set = (key, value) => {
    sessionStorage.setItem(key, value)
  }

  const get = (key) => {
    return sessionStorage.getItem(key)
  }

  const remove = (key) => {
    sessionStorage.removeItem(key)
  }

  const clear = () => {
    sessionStorage.clear()
  }

  return {
    set,
    get,
    remove,
    clear,
  }
}

export const useSessionStorage = createSessionStorage()

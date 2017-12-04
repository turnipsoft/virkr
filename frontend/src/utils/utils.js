export function wrap(text, width) {
  if (text.length>width) {
    const index = text.indexOf(' ');
    return '\n' + text.substring(0, index) + '\n' + text.substring(index+1);
  }

  return text;
}

/**
 * Anvendes til at at resolvere en json værdi baseret på en path, så man kan bruge . notation til at hente en værdi
 * fra et JSON object
 * @param path
 * @param obj
 * @returns {*}
 */
export function resolveJsonValue(path, obj) {
  return path.split('.').reduce(function(prev, curr) {
    return prev ? prev[curr] : undefined
  }, obj || self)
}


export function komma(vaerdi) {
  if (vaerdi) {
    const v = vaerdi.toLocaleString();
    return v.replace(/,/g, ".");
  }

  return vaerdi;
}

export function wrap(text, width) {
  if (text.length>width) {
    const index = text.indexOf(' ');
    return '\n' + text.substring(0, index) + '\n' + text.substring(index+1);
  }

  return text;
}

# export the terminal size to env
export LINES COLUMNS

# set terminal to raw mode (to get raw key strokes) and restore after running the app
# see man stty
save_state=$(stty -g)
stty raw
scala JumpRun.scala
stty "$save_state"
